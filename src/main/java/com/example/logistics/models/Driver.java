package com.example.logistics.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    @Column(nullable = false, length = 100)
    private String driverName;

    @Column(nullable = false, unique = true, length = 100)
    private String licenseNumber;

    @Column(nullable = false, length = 100)
    private String vehicleType;

    // Removed updatable = false, and defaulted it to true for new drivers
    @Column(name = "is_available")
    private Boolean isAvailable = true; 
}