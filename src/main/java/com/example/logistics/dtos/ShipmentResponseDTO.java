package com.example.logistics.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShipmentResponseDTO {

    private Long shipmentId;
    private String recipientName;
    private String deliveryAddress;
    private BigDecimal packageWeight;
    private BigDecimal shippingFee;
    private String currentStatus;
    private LocalDateTime createdAt;

    private Long customerId;
    private String customerName;
    private String customerEmail;

    private Long driverId;
    private String driverName;
    private String vehicleType;

    public ShipmentResponseDTO() {}

    public ShipmentResponseDTO(Long shipmentId,
                                String recipientName,
                                String deliveryAddress,
                                BigDecimal packageWeight,
                                BigDecimal shippingFee,
                                String currentStatus,
                                LocalDateTime createdAt,
                                Long customerId,
                                String customerName,
                                String customerEmail,
                                Long driverId,
                                String driverName,
                                String vehicleType) {
        this.shipmentId = shipmentId;
        this.recipientName = recipientName;
        this.deliveryAddress = deliveryAddress;
        this.packageWeight = packageWeight;
        this.shippingFee = shippingFee;
        this.currentStatus = currentStatus;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.driverId = driverId;
        this.driverName = driverName;
        this.vehicleType = vehicleType;
    }

    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public BigDecimal getPackageWeight() { return packageWeight; }
    public void setPackageWeight(BigDecimal packageWeight) { this.packageWeight = packageWeight; }

    public BigDecimal getShippingFee() { return shippingFee; }
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}