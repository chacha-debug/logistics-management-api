package com.example.logistics.repositories;

import com.example.logistics.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Spring Boot automatically gives us CRUD here!
}