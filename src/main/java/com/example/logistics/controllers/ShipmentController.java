package com.example.logistics.controllers;

import com.example.logistics.models.Shipment;
import com.example.logistics.repositories.ShipmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(@PathVariable Long id) {
        return shipmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Returns 404 Not Found cleanly
    }

    @PostMapping
    public ResponseEntity<Shipment> createShipment(@Valid @RequestBody Shipment shipment) {
        Shipment savedShipment = shipmentRepository.save(shipment);
        return ResponseEntity.ok(savedShipment);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Shipment>> searchShipments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name) {

        if (status != null && !status.isBlank() && name != null && !name.isBlank()) {
            return ResponseEntity.ok(shipmentRepository.findByCurrentStatusIgnoreCaseAndRecipientNameContainingIgnoreCase(status, name));
        } else if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(shipmentRepository.findByCurrentStatusIgnoreCase(status));
        } else if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(shipmentRepository.findByRecipientNameContainingIgnoreCase(name));
        }

        return ResponseEntity.ok(shipmentRepository.findAll());
    }
}