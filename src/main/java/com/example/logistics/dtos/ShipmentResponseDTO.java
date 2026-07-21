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
    private Long customerId;
    private Long driverId;

    public ShipmentResponseDTO() {}

    public ShipmentResponseDTO(Long shipmentId, String recipientName, String deliveryAddress, 
                               BigDecimal packageWeight, BigDecimal shippingFee, String currentStatus, 
                               Long customerId, Long driverId) {
        this.shipmentId = shipmentId;
        this.recipientName = recipientName;
        this.deliveryAddress = deliveryAddress;
        this.packageWeight = packageWeight;
        this.shippingFee = shippingFee;
        this.currentStatus = currentStatus;
        this.customerId = customerId;
        this.driverId = driverId;
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

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
}