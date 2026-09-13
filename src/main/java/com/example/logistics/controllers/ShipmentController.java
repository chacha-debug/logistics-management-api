package com.example.logistics.controllers;

import com.example.logistics.dtos.ShipmentRequestDTO;
import com.example.logistics.dtos.ShipmentResponseDTO;
import com.example.logistics.services.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;



@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private static final Logger log = LoggerFactory.getLogger(ShipmentController.class);

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponseDTO>> getAllShipments() {
        log.info("GET /api/shipments - fetching all shipments");
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDTO> getShipmentById(@PathVariable Long id) {
        log.info("GET /api/shipments/{} - fetching shipment by ID", id);
        return ResponseEntity.ok(shipmentService.getShipmentById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShipmentResponseDTO>> searchShipments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name) {
        log.info("GET /api/shipments/search - searching shipments with status: {}, name: {}", status, name);
        return ResponseEntity.ok(shipmentService.searchShipments(status, name));
    }

    @PostMapping
    public ResponseEntity<ShipmentResponseDTO> createShipment(@Valid @RequestBody ShipmentRequestDTO shipmentRequestDTO) {
        log.info("POST /api/shipments - creating shipment for recipient: {}", shipmentRequestDTO.getRecipientName());
        ShipmentResponseDTO createdShipment = shipmentService.createShipment(shipmentRequestDTO);
        return new ResponseEntity<>(createdShipment, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
public ResponseEntity<ShipmentResponseDTO> updateStatus(
        @PathVariable Long id,
        @RequestParam String status,
        @RequestParam(required = false) String remarks) {
    log.info("PATCH /api/shipments/{}/status - updating status for shipment: {}", id, status);
    return ResponseEntity.ok(shipmentService.updateStatus(id, status, remarks));
}
}