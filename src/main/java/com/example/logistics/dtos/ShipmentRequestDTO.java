package com.example.logistics.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ShipmentRequestDTO {

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotNull(message = "Package weight is required")
    @Positive(message = "Package weight must be greater than zero")
    private BigDecimal packageWeight;

    @NotBlank(message = "Status is required")
    private String currentStatus;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Driver ID is required")
    private Long driverId;

    public ShipmentRequestDTO() {}

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public BigDecimal getPackageWeight() { return packageWeight; }
    public void setPackageWeight(BigDecimal packageWeight) { this.packageWeight = packageWeight; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
}