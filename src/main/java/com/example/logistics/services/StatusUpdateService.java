package com.example.logistics.services;

import com.example.logistics.models.Shipment;
import com.example.logistics.models.StatusUpdate;
import com.example.logistics.repositories.ShipmentRepository;
import com.example.logistics.repositories.StatusUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StatusUpdateService {

    @Autowired
    private StatusUpdateRepository statusUpdateRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    public List<StatusUpdate> getAllStatusUpdates() {
        return statusUpdateRepository.findAll();
    }

    public List<StatusUpdate> getUpdatesForShipment(Long shipmentId) {
        return statusUpdateRepository.findByShipmentShipmentIdOrderByChangedAtDesc(shipmentId);
    }

    @Transactional
    public StatusUpdate addStatusUpdate(StatusUpdate statusUpdate) {
        // 1. Fetch the actual shipment from the database
        Long shipmentId = statusUpdate.getShipment().getShipmentId();
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with ID: " + shipmentId));

        // 2. Attach full shipment entity to the status update
        statusUpdate.setShipment(shipment);

        // 3. Save the new status update record
        StatusUpdate savedUpdate = statusUpdateRepository.save(statusUpdate);

        // 4. Update the shipment's current status automatically
        shipment.setCurrentStatus(statusUpdate.getStatus());
        shipmentRepository.save(shipment);

        return savedUpdate;
    }
}