package com.example.logistics.controllers;

import com.example.logistics.models.StatusUpdate;
import com.example.logistics.services.StatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-updates")
public class StatusUpdateController {

    @Autowired
    private StatusUpdateService statusUpdateService;

    @GetMapping
    public List<StatusUpdate> getAllStatusUpdates() {
        return statusUpdateService.getAllStatusUpdates();
    }

    @GetMapping("/shipment/{shipmentId}")
    public List<StatusUpdate> getUpdatesForShipment(@PathVariable Long shipmentId) {
        return statusUpdateService.getUpdatesForShipment(shipmentId);
    }

    @PostMapping
    public ResponseEntity<StatusUpdate> createStatusUpdate(@RequestBody StatusUpdate statusUpdate) {
        StatusUpdate savedUpdate = statusUpdateService.addStatusUpdate(statusUpdate);
        return ResponseEntity.ok(savedUpdate);
    }
}