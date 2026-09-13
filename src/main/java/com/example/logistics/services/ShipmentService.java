package com.example.logistics.services;

import com.example.logistics.dtos.ShipmentRequestDTO;
import com.example.logistics.dtos.ShipmentResponseDTO;
import com.example.logistics.exceptions.ResourceNotFoundException;
import com.example.logistics.models.Customer;
import com.example.logistics.models.Driver;
import com.example.logistics.models.Shipment;
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

    @Transactional
    public ShipmentResponseDTO createShipment(ShipmentRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + requestDTO.getCustomerId()));
        Driver driver = driverRepository.findById(requestDTO.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver not found with ID: " + requestDTO.getDriverId()));

        Shipment shipment = new Shipment();
        shipment.setCustomer(customer);
        shipment.setDriver(driver);
        shipment.setRecipientName(requestDTO.getRecipientName());
        shipment.setDeliveryAddress(requestDTO.getDeliveryAddress());
        shipment.setPackageWeight(requestDTO.getPackageWeight());
        shipment.setCurrentStatus(requestDTO.getCurrentStatus() != null
                ? requestDTO.getCurrentStatus() : "Pending");

        Shipment savedShipment = shipmentRepository.save(shipment);

        historyRepository.save(new ShipmentStatusHistory(
                savedShipment,
                savedShipment.getCurrentStatus(),
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
        return new ShipmentResponseDTO(
                shipment.getShipmentId(),
                shipment.getRecipientName(),
                shipment.getDeliveryAddress(),
                shipment.getPackageWeight(),
                fee,
                shipment.getCurrentStatus(),
                shipment.getCustomer() != null ? shipment.getCustomer().getCustomerId() : null,
                shipment.getDriver() != null ? shipment.getDriver().getDriverId() : null
        );
    }
}