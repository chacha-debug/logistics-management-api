package com.example.logistics.controllers;

import com.example.logistics.models.Customer;
import com.example.logistics.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers") // Base URL route for this controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    // GET Request to fetch all customers: http://localhost:8080/api/customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // POST Request to create a new customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(savedCustomer);
    }
}