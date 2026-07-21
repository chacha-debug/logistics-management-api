package com.example.logistics.services;

import com.example.logistics.dtos.ShipmentRequestDTO;
import com.example.logistics.dtos.ShipmentResponseDTO;
import com.example.logistics.models.Shipment;
import com.example.logistics.repositories.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public List<ShipmentResponseDTO> getAllShipments() {
        return shipmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ShipmentResponseDTO getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + id));
        return convertToDTO(shipment);
    }

    public ShipmentResponseDTO createShipment(ShipmentRequestDTO requestDTO) {
        Shipment shipment = new Shipment();
        shipment.setRecipientName(requestDTO.getRecipientName());
        shipment.setDeliveryAddress(requestDTO.getDeliveryAddress());
        shipment.setPackageWeight(requestDTO.getPackageWeight());
        shipment.setCurrentStatus(requestDTO.getCurrentStatus());

        Shipment savedShipment = shipmentRepository.save(shipment);
        return convertToDTO(savedShipment);
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

    // Entity -> DTO Conversion Mapping
    private ShipmentResponseDTO convertToDTO(Shipment shipment) {
        return new ShipmentResponseDTO(
                shipment.getShipmentId(),
                shipment.getRecipientName(),
                shipment.getDeliveryAddress(),
                shipment.getPackageWeight(),
                shipment.getCurrentStatus(),
                shipment.getCustomer() != null ? shipment.getCustomer().getCustomerId() : null,
                shipment.getDriver() != null ? shipment.getDriver().getDriverId() : null
        );
    }
}