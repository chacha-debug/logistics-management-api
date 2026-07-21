package com.example.logistics.services;

import com.example.logistics.dtos.ShipmentRequestDTO;
import com.example.logistics.dtos.ShipmentResponseDTO;
import com.example.logistics.exceptions.ResourceNotFoundException;
import com.example.logistics.models.Shipment;
import com.example.logistics.models.ShipmentStatusHistory;
import com.example.logistics.repositories.ShipmentRepository;
import com.example.logistics.repositories.ShipmentStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private static final BigDecimal BASE_FEE = new BigDecimal("50.00");
    private static final BigDecimal RATE_PER_KG = new BigDecimal("12.50");

    private final ShipmentRepository shipmentRepository;
    private final ShipmentStatusHistoryRepository historyRepository;

    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentStatusHistoryRepository historyRepository) {
        this.shipmentRepository = shipmentRepository;
        this.historyRepository = historyRepository;
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

    public ShipmentResponseDTO createShipment(ShipmentRequestDTO requestDTO) {
        Shipment shipment = new Shipment();
        shipment.setRecipientName(requestDTO.getRecipientName());
        shipment.setDeliveryAddress(requestDTO.getDeliveryAddress());
        shipment.setPackageWeight(requestDTO.getPackageWeight());
        shipment.setCurrentStatus(requestDTO.getCurrentStatus());

        Shipment savedShipment = shipmentRepository.save(shipment);

        // Record initial status history audit entry
        historyRepository.save(new ShipmentStatusHistory(
                savedShipment, 
                savedShipment.getCurrentStatus(), 
                LocalDateTime.now(), 
                "Shipment registered in system."
        ));

        return convertToDTO(savedShipment);
    }

    public ShipmentResponseDTO updateStatus(Long shipmentId, String newStatus, String remarks) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with ID: " + shipmentId));

        shipment.setCurrentStatus(newStatus);
        Shipment updated = shipmentRepository.save(shipment);

        // Record audit trail
        historyRepository.save(new ShipmentStatusHistory(
                updated, 
                newStatus, 
                LocalDateTime.now(), 
                remarks != null ? remarks : "Status updated to " + newStatus
        ));

        return convertToDTO(updated);
    }

    public List<ShipmentResponseDTO> searchShipments(String status, String name) {
        List<Shipment> shipments;

        if (status != null && !status.isBlank() && name != null && !name.isBlank()) {
            shipments = shipmentRepository.findByCurrentStatusIgnoreCaseAndRecipientNameContainingIgnoreCase(status, name);
        } else if (status != null && !status.isBlank()) {
            shipments = shipmentRepository.findByCurrentStatusIgnoreCase(status);
        } else if (name != null && !name.isBlank()) {
            shipments = shipmentRepository.findByRecipientNameContainingIgnoreCase(name);
        } else {
            shipments = shipmentRepository.findAll();
        }

        return shipments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Dynamic fee calculation: Base Fee + (Weight * Rate)
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