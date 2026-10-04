package com.example.logistics.services;

import com.example.logistics.dtos.ShipmentDetailDTO;
import com.example.logistics.dtos.ShipmentRequestDTO;
import com.example.logistics.dtos.ShipmentResponseDTO;
import com.example.logistics.exceptions.ResourceNotFoundException;
import com.example.logistics.models.Customer;
import com.example.logistics.models.Driver;
import com.example.logistics.models.Shipment;
import com.example.logistics.models.ShipmentStatus;
import com.example.logistics.models.ShipmentStatusHistory;
import com.example.logistics.repositories.CustomerRepository;
import com.example.logistics.repositories.DriverRepository;
import com.example.logistics.repositories.ShipmentRepository;
import com.example.logistics.repositories.ShipmentStatusHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    private static final BigDecimal BASE_FEE = new BigDecimal("50.00");
    private static final BigDecimal RATE_PER_KG = new BigDecimal("12.50");

    private final ShipmentRepository shipmentRepository;
    private final ShipmentStatusHistoryRepository historyRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    public ShipmentService(ShipmentRepository shipmentRepository,
                           ShipmentStatusHistoryRepository historyRepository,
                           CustomerRepository customerRepository,
                           DriverRepository driverRepository) {
        this.shipmentRepository = shipmentRepository;
        this.historyRepository = historyRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
    }

    public List<ShipmentResponseDTO> getAllShipments() {
        return shipmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ShipmentResponseDTO getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with ID: " + id));
        return convertToDTO(shipment);
    }

    /**
     * Returns a shipment with its full status history.
     */
    public ShipmentDetailDTO getShipmentDetails(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with ID: " + id));

        List<ShipmentDetailDTO.StatusHistoryEntry> history =
                historyRepository.findByShipmentShipmentIdOrderByTimestampDesc(id)
                        .stream()
                        .map(h -> new ShipmentDetailDTO.StatusHistoryEntry(
                                h.getStatus(), h.getTimestamp(), h.getRemarks()))
                        .collect(Collectors.toList());

        return new ShipmentDetailDTO(convertToDTO(shipment), history);
    }

    @Transactional
    public ShipmentResponseDTO createShipment(ShipmentRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + requestDTO.getCustomerId()));
        Driver driver = driverRepository.findById(requestDTO.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver not found with ID: " + requestDTO.getDriverId()));

        String initialStatus = requestDTO.getCurrentStatus() != null
                && ShipmentStatus.isValidStatus(requestDTO.getCurrentStatus())
                ? requestDTO.getCurrentStatus()
                : ShipmentStatus.PENDING;

        Shipment shipment = new Shipment();
        shipment.setCustomer(customer);
        shipment.setDriver(driver);
        shipment.setRecipientName(requestDTO.getRecipientName());
        shipment.setDeliveryAddress(requestDTO.getDeliveryAddress());
        shipment.setPackageWeight(requestDTO.getPackageWeight());
        shipment.setCurrentStatus(initialStatus);

        Shipment savedShipment = shipmentRepository.save(shipment);

        historyRepository.save(new ShipmentStatusHistory(
                savedShipment,
                initialStatus,
                LocalDateTime.now(),
                "Shipment registered in system."
        ));

        log.info("Created shipment {} for customer {} (recipient: {})",
                savedShipment.getShipmentId(), customer.getCustomerId(), shipment.getRecipientName());

        return convertToDTO(savedShipment);
    }

    @Transactional
    public ShipmentResponseDTO updateStatus(Long shipmentId, String newStatus, String remarks) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with ID: " + shipmentId));

        String previousStatus = shipment.getCurrentStatus();

        if (!ShipmentStatus.isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Unknown status: " + newStatus);
        }
        if (!ShipmentStatus.isValidTransition(previousStatus, newStatus)) {
            throw new IllegalStateException(
                    "Invalid transition from " + previousStatus + " to " + newStatus);
        }

        shipment.setCurrentStatus(newStatus);
        Shipment updated = shipmentRepository.save(shipment);

        historyRepository.save(new ShipmentStatusHistory(
                updated,
                newStatus,
                LocalDateTime.now(),
                remarks != null ? remarks : "Status updated to " + newStatus
        ));

        log.info("Shipment {} status changed: {} → {}", shipmentId, previousStatus, newStatus);

        return convertToDTO(updated);
    }

    /**
     * Reassigns a shipment to a different driver.
     */
    @Transactional
    public ShipmentResponseDTO assignDriver(Long shipmentId, Long driverId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with ID: " + shipmentId));
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        Long previousDriverId = shipment.getDriver() != null
                ? shipment.getDriver().getDriverId() : null;

        shipment.setDriver(driver);
        Shipment updated = shipmentRepository.save(shipment);

        historyRepository.save(new ShipmentStatusHistory(
                updated,
                updated.getCurrentStatus(),
                LocalDateTime.now(),
                "Driver reassigned from #" + previousDriverId + " to #" + driverId
                        + " (" + driver.getDriverName() + ")"
        ));

        log.info("Shipment {} reassigned from driver {} to {}", shipmentId, previousDriverId, driverId);

        return convertToDTO(updated);
    }

    public List<ShipmentResponseDTO> searchShipments(String status, String name) {
        List<Shipment> shipments;

        boolean hasStatus = status != null && !status.isBlank();
        boolean hasName = name != null && !name.isBlank();

        if (hasStatus && hasName) {
            shipments = shipmentRepository.findByCurrentStatusIgnoreCaseAndRecipientNameContainingIgnoreCase(status, name);
        } else if (hasStatus) {
            shipments = shipmentRepository.findByCurrentStatusIgnoreCase(status);
        } else if (hasName) {
            shipments = shipmentRepository.findByRecipientNameContainingIgnoreCase(name);
        } else {
            shipments = shipmentRepository.findAll();
        }

        return shipments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateFee(BigDecimal weight) {
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            return BASE_FEE;
        }
        return BASE_FEE.add(weight.multiply(RATE_PER_KG));
    }

    private ShipmentResponseDTO convertToDTO(Shipment shipment) {
        BigDecimal fee = calculateFee(shipment.getPackageWeight());

        Customer c = shipment.getCustomer();
        Driver d = shipment.getDriver();

        return new ShipmentResponseDTO(
                shipment.getShipmentId(),
                shipment.getRecipientName(),
                shipment.getDeliveryAddress(),
                shipment.getPackageWeight(),
                fee,
                shipment.getCurrentStatus(),
                shipment.getCreatedAt(),
                c != null ? c.getCustomerId() : null,
                c != null ? c.getFullName() : null,
                c != null ? c.getEmail() : null,
                d != null ? d.getDriverId() : null,
                d != null ? d.getDriverName() : null,
                d != null ? d.getVehicleType() : null
        );
    }
}