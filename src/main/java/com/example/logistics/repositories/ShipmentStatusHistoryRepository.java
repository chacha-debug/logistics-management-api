package com.example.logistics.repositories;

import com.example.logistics.models.ShipmentStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShipmentStatusHistoryRepository extends JpaRepository<ShipmentStatusHistory, Long> {
    List<ShipmentStatusHistory> findByShipmentShipmentIdOrderByTimestampDesc(Long shipmentId);
}