package com.example.logistics.controllers;

import com.example.logistics.dtos.ShipmentRequestDTO;
import com.example.logistics.dtos.ShipmentResponseDTO;
import com.example.logistics.services.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponseDTO>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDTO> getShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.getShipmentById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShipmentResponseDTO>> searchShipments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(shipmentService.searchShipments(status, name));
    }

    @PostMapping
    public ResponseEntity<ShipmentResponseDTO> createShipment(@Valid @RequestBody ShipmentRequestDTO shipmentRequestDTO) {
        ShipmentResponseDTO createdShipment = shipmentService.createShipment(shipmentRequestDTO);
        return new ResponseEntity<>(createdShipment, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
public ResponseEntity<ShipmentResponseDTO> updateStatus(
        @PathVariable Long id,
        @RequestParam String status,
        @RequestParam(required = false) String remarks) {
    return ResponseEntity.ok(shipmentService.updateStatus(id, status, remarks));
}
}