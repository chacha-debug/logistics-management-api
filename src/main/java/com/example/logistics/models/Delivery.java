package com.example.logistics.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne
    @JoinColumn(name = "shipment_id", nullable = false, unique = true)
    private Shipment shipment;

    @Column(name = "tracking_number", unique = true, nullable = false)
    private String trackingNumber;

    private LocalDateTime dispatchTime;

    private LocalDateTime estimatedDelivery;

    private LocalDateTime deliveredAt;
}