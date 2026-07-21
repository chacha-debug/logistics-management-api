package com.example.logistics.repositories;

import com.example.logistics.models.StatusUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusUpdateRepository extends JpaRepository<StatusUpdate, Long> {
    // Custom query method to find tracking history for a specific shipment ordered by date
    List<StatusUpdate> findByShipmentShipmentIdOrderByChangedAtDesc(Long shipmentId);
}