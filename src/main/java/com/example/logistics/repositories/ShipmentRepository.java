package com.example.logistics.repositories;

import com.example.logistics.models.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    // Filter by exact status (e.g., "Pending", "Out for Delivery")
    List<Shipment> findByCurrentStatusIgnoreCase(String currentStatus);

    // Search recipient name containing keyword (case-insensitive)
    List<Shipment> findByRecipientNameContainingIgnoreCase(String recipientName);

    // Multi-criteria search: filter by status AND recipient name keyword
    List<Shipment> findByCurrentStatusIgnoreCaseAndRecipientNameContainingIgnoreCase(String currentStatus, String recipientName);
}