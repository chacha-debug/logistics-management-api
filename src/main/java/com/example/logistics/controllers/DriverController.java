package com.example.logistics.controllers;

import com.example.logistics.models.Driver;
import com.example.logistics.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers") // Base URL route for this controller
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    // GET Request to fetch all drivers: http://localhost:8080/api/drivers
    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    // POST Request to create a new driver
    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver driver) {
        Driver savedDriver = driverRepository.save(driver);
        return ResponseEntity.ok(savedDriver);
    }
}