package com.example.logistics.config;

import com.example.logistics.models.Customer;
import com.example.logistics.models.Driver;
import com.example.logistics.models.Shipment;
import com.example.logistics.repositories.CustomerRepository;
import com.example.logistics.repositories.DriverRepository;
import com.example.logistics.repositories.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@Configuration
@Profile("!test") // don't seed during tests
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    CommandLineRunner seedData(
            CustomerRepository customerRepo,
            DriverRepository driverRepo,
            ShipmentRepository shipmentRepo) {
        return args -> {
            if (customerRepo.count() > 0) {
                log.info("Database already seeded — skipping");
                return;
            }

            log.info("Seeding demo data...");

            Customer alice = customerRepo.save(new Customer(null, "Alice Smith", "alice@example.com", "+27123456789", null));
            Customer bob = customerRepo.save(new Customer(null, "Bob Marley", "bob@example.com", "+27987654321", null));
            Customer test = customerRepo.save(new Customer(null, "Test Customer", "test@example.com", "+27000000000", null));

            Driver john = driverRepo.save(new Driver(null, "John Doe", "DL-12345", "Van", true));
            Driver sarah = driverRepo.save(new Driver(null, "Sarah Mokoena", "DL-67890", "Truck", true));
            Driver thabo = driverRepo.save(new Driver(null, "Thabo Nkosi", "DL-11111", "Motorcycle", true));

            Shipment s1 = new Shipment();
            s1.setCustomer(alice);
            s1.setDriver(john);
            s1.setRecipientName("Jane Doe");
            s1.setDeliveryAddress("12 Main Street, Kimberley");
            s1.setPackageWeight(new BigDecimal("5.50"));
            s1.setCurrentStatus("Pending");
            shipmentRepo.save(s1);

            Shipment s2 = new Shipment();
            s2.setCustomer(bob);
            s2.setDriver(sarah);
            s2.setRecipientName("Peter Pan");
            s2.setDeliveryAddress("45 Oak Avenue, Johannesburg");
            s2.setPackageWeight(new BigDecimal("12.00"));
            s2.setCurrentStatus("In Transit");
            shipmentRepo.save(s2);

            log.info("Demo data seeded: {} customers, {} drivers, {} shipments",
                    customerRepo.count(), driverRepo.count(), shipmentRepo.count());
        };
    }
}