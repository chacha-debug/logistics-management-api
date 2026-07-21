package com.example.logistics.repositories;

import com.example.logistics.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    // Spring Boot automatically gives us CRUD here
}