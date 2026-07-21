package com.example.logistics.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    @NotNull(message = "Customer is required")
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Driver is required")
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @NotNull(message = "Package weight is required")
    @Positive(message = "Weight must be greater than zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal packageWeight;

    @NotBlank(message = "Recipient name cannot be blank")
    @Column(nullable = false, length = 100)
    private String recipientName;

    @NotBlank(message = "Delivery address cannot be blank")
    @Column(nullable = false, length = 255)
    private String deliveryAddress;

    @Column(nullable = false, length = 50)
    private String currentStatus = "Pending";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}