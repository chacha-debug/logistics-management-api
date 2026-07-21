package com.example.logistics.services;

import com.example.logistics.models.Shipment;
import com.example.logistics.models.StatusUpdate;
import com.example.logistics.repositories.ShipmentRepository;
import com.example.logistics.repositories.StatusUpdateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusUpdateServiceTest {

    @Mock
    private StatusUpdateRepository statusUpdateRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private StatusUpdateService statusUpdateService;

    private Shipment existingShipment;
    private StatusUpdate newUpdate;

    @BeforeEach
    void setUp() {
        existingShipment = new Shipment();
        existingShipment.setShipmentId(1L);
        existingShipment.setCurrentStatus("Pending");

        newUpdate = new StatusUpdate();
        newUpdate.setShipment(existingShipment);
        newUpdate.setStatus("Out for Delivery");
        newUpdate.setLocation("Kimberley Depot");
    }

    @Test
    void addStatusUpdate_ShouldUpdateShipmentStatusAndSave() {
        // Mock repository behavior
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(existingShipment));
        when(statusUpdateRepository.save(any(StatusUpdate.class))).thenReturn(newUpdate);

        // Execute service method
        StatusUpdate result = statusUpdateService.addStatusUpdate(newUpdate);

        // Assertions
        assertNotNull(result);
        assertEquals("Out for Delivery", result.getStatus());
        assertEquals("Out for Delivery", existingShipment.getCurrentStatus());

        // Verify interactions
        verify(shipmentRepository, times(1)).findById(1L);
        verify(shipmentRepository, times(1)).save(existingShipment);
        verify(statusUpdateRepository, times(1)).save(newUpdate);
    }
}