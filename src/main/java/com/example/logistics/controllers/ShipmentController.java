package com.example.logistics.controllers;

import com.example.logistics.dtos.ShipmentDetailDTO;
import com.example.logistics.dtos.ShipmentRequestDTO;
import com.example.logistics.dtos.ShipmentResponseDTO;
import com.example.logistics.services.ShipmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private static final Logger log = LoggerFactory.getLogger(ShipmentController.class);

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponseDTO>> getAllShipments() {
        log.info("GET /api/shipments");
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDTO> getShipmentById(@PathVariable Long id) {
        log.info("GET /api/shipments/{}", id);
        return ResponseEntity.ok(shipmentService.getShipmentById(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ShipmentDetailDTO> getShipmentDetails(@PathVariable Long id) {
        log.info("GET /api/shipments/{}/details", id);
        return ResponseEntity.ok(shipmentService.getShipmentDetails(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShipmentResponseDTO>> searchShipments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name) {
        log.info("GET /api/shipments/search - status: {}, name: {}", status, name);
        return ResponseEntity.ok(shipmentService.searchShipments(status, name));
    }

    @PostMapping
    public ResponseEntity<ShipmentResponseDTO> createShipment(
            @Valid @RequestBody ShipmentRequestDTO shipmentRequestDTO) {
        log.info("POST /api/shipments - recipient: {}", shipmentRequestDTO.getRecipientName());
        ShipmentResponseDTO created = shipmentService.createShipment(shipmentRequestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String remarks) {
        log.info("PATCH /api/shipments/{}/status - new status: {}", id, status);
        return ResponseEntity.ok(shipmentService.updateStatus(id, status, remarks));
    }

    @PatchMapping("/{id}/assign-driver")
    public ResponseEntity<ShipmentResponseDTO> assignDriver(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        log.info("PATCH /api/shipments/{}/assign-driver - driverId: {}", id, driverId);
        return ResponseEntity.ok(shipmentService.assignDriver(id, driverId));
    }
}