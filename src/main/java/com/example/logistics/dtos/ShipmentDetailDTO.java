package com.example.logistics.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class ShipmentDetailDTO {

    private ShipmentResponseDTO shipment;
    private List<StatusHistoryEntry> statusHistory;

    public ShipmentDetailDTO() {}

    public ShipmentDetailDTO(ShipmentResponseDTO shipment, List<StatusHistoryEntry> statusHistory) {
        this.shipment = shipment;
        this.statusHistory = statusHistory;
    }

    public ShipmentResponseDTO getShipment() { return shipment; }
    public void setShipment(ShipmentResponseDTO shipment) { this.shipment = shipment; }

    public List<StatusHistoryEntry> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<StatusHistoryEntry> statusHistory) { this.statusHistory = statusHistory; }

    public static class StatusHistoryEntry {
        private String status;
        private LocalDateTime timestamp;
        private String remarks;

        public StatusHistoryEntry() {}

        public StatusHistoryEntry(String status, LocalDateTime timestamp, String remarks) {
            this.status = status;
            this.timestamp = timestamp;
            this.remarks = remarks;
        }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }
}