package com.example.logistics.config;

import com.example.logistics.models.Customer;
import com.example.logistics.models.Driver;
import com.example.logistics.models.Shipment;
import com.example.logistics.models.ShipmentStatusHistory;
import com.example.logistics.repositories.CustomerRepository;
import com.example.logistics.repositories.DriverRepository;
import com.example.logistics.repositories.ShipmentRepository;
import com.example.logistics.repositories.ShipmentStatusHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile("!test")
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    CommandLineRunner seedData(
            CustomerRepository customerRepo,
            DriverRepository driverRepo,
            ShipmentRepository shipmentRepo,
            ShipmentStatusHistoryRepository historyRepo) {
        return args -> {
            if (customerRepo.count() > 0) {
                log.info("Database already seeded - skipping");
                return;
            }

            log.info("Seeding demo data...");

            // ============= CUSTOMERS =============
            List<Customer> customers = List.of(
                    new Customer(null, "Alice Smith", "alice.smith@example.com", "+27123456789", null),
                    new Customer(null, "Bob Marley", "bob.marley@example.com", "+27987654321", null),
                    new Customer(null, "Thandiwe Dlamini", "thandiwe.d@example.com", "+27821112233", null),
                    new Customer(null, "Sipho Nkosi", "sipho.nkosi@example.com", "+27834445566", null),
                    new Customer(null, "Lerato Mokoena", "lerato.m@example.com", "+27725557788", null),
                    new Customer(null, "James van der Merwe", "james.vdm@example.com", "+27616669900", null),
                    new Customer(null, "Aisha Patel", "aisha.patel@example.com", "+27771112244", null),
                    new Customer(null, "Kabelo Mthembu", "kabelo.m@example.com", "+27782223355", null),
                    new Customer(null, "Michelle Naidoo", "michelle.n@example.com", "+27833334466", null),
                    new Customer(null, "Pieter Botha", "pieter.botha@example.com", "+27844445577", null)
            );
            customers = customerRepo.saveAll(customers);

            // ============= DRIVERS =============
            List<Driver> drivers = List.of(
                    new Driver(null, "John Doe", "DL-12345", "Van", true),
                    new Driver(null, "Sarah Mokoena", "DL-67890", "Truck", true),
                    new Driver(null, "Thabo Nkosi", "DL-11111", "Motorcycle", true),
                    new Driver(null, "Nomvula Zulu", "DL-22222", "Van", true),
                    new Driver(null, "Ahmed Khan", "DL-33333", "Truck", false)
            );
            drivers = driverRepo.saveAll(drivers);

            // ============= SHIPMENTS =============
            // Create shipments with realistic timestamps spread over the past 3 weeks
            LocalDateTime now = LocalDateTime.now();
            List<Shipment> shipments = List.of(
                    buildShipment(customers.get(0), drivers.get(0), "Jane Doe",
                            "12 Main Street, Kimberley", "5.50", "Delivered",
                            now.minusDays(21)),
                    buildShipment(customers.get(1), drivers.get(1), "Peter Pan",
                            "45 Oak Avenue, Johannesburg", "12.00", "Delivered",
                            now.minusDays(20)),
                    buildShipment(customers.get(2), drivers.get(2), "Sibusiso Khumalo",
                            "78 Long Street, Cape Town", "2.30", "In Transit",
                            now.minusDays(18)),
                    buildShipment(customers.get(3), drivers.get(3), "Nomsa Mahlangu",
                            "22 Church Street, Pretoria", "8.75", "In Transit",
                            now.minusDays(15)),
                    buildShipment(customers.get(4), drivers.get(0), "Tshepo Modise",
                            "34 Kloof Street, Cape Town", "15.20", "Out for Delivery",
                            now.minusDays(12)),
                    buildShipment(customers.get(5), drivers.get(1), "Riaan Steyn",
                            "56 Beach Road, Durban", "25.00", "Out for Delivery",
                            now.minusDays(10)),
                    buildShipment(customers.get(6), drivers.get(2), "Fatima Ahmed",
                            "89 Voortrekker Road, Bellville", "1.80", "Pending",
                            now.minusDays(8)),
                    buildShipment(customers.get(7), drivers.get(3), "Lindiwe Sithole",
                            "101 Nelson Mandela Drive, Bloemfontein", "18.50", "Pending",
                            now.minusDays(7)),
                    buildShipment(customers.get(8), drivers.get(0), "Mark Johnson",
                            "14 Adderley Street, Cape Town", "7.25", "Pending",
                            now.minusDays(5)),
                    buildShipment(customers.get(9), drivers.get(1), "Zanele Mbeki",
                            "33 Beyers Naude Drive, Johannesburg", "42.00", "Cancelled",
                            now.minusDays(4)),
                    buildShipment(customers.get(0), drivers.get(2), "Precious Mahlaba",
                            "9 West Street, Durban", "3.40", "Pending",
                            now.minusDays(3)),
                    buildShipment(customers.get(1), drivers.get(3), "Kagiso Molefe",
                            "120 Main Road, Port Elizabeth", "22.10", "In Transit",
                            now.minusDays(2)),
                    buildShipment(customers.get(2), drivers.get(0), "Naledi Khumalo",
                            "47 President Street, Johannesburg", "6.60", "Pending",
                            now.minusDays(1)),
                    buildShipment(customers.get(3), drivers.get(1), "Andile Dube",
                            "58 Samora Machel Drive, Nelspruit", "14.30", "Pending",
                            now.minusHours(8)),
                    buildShipment(customers.get(4), drivers.get(2), "Refilwe Motaung",
                            "6 Boom Street, Pietermaritzburg", "2.90", "Pending",
                            now.minusHours(2))
            );
            shipments = shipmentRepo.saveAll(shipments);

            // ============= STATUS HISTORY =============
            // Build a history entry for every shipment based on its current status
            for (Shipment s : shipments) {
                LocalDateTime created = s.getCreatedAt();
                String currentStatus = s.getCurrentStatus();

                // Always has "created" entry
                historyRepo.save(new ShipmentStatusHistory(
                        s, "Pending", created,
                        "Shipment registered in system"));

                // Add intermediate transitions based on status
                if ("In Transit".equals(currentStatus) || "Out for Delivery".equals(currentStatus) || "Delivered".equals(currentStatus)) {
                    historyRepo.save(new ShipmentStatusHistory(
                            s, "Confirmed", created.plusHours(2),
                            "Shipment confirmed by dispatcher"));
                    historyRepo.save(new ShipmentStatusHistory(
                            s, "In Transit", created.plusHours(6),
                            "Package picked up and in transit"));
                }

                if ("Out for Delivery".equals(currentStatus) || "Delivered".equals(currentStatus)) {
                    historyRepo.save(new ShipmentStatusHistory(
                            s, "Out for Delivery", created.plusDays(1),
                            "Out for delivery — driver en route"));
                }

                if ("Delivered".equals(currentStatus)) {
                    historyRepo.save(new ShipmentStatusHistory(
                            s, "Delivered", created.plusDays(1).plusHours(4),
                            "Delivered successfully to recipient"));
                }

                if ("Cancelled".equals(currentStatus)) {
                    historyRepo.save(new ShipmentStatusHistory(
                            s, "Cancelled", created.plusHours(3),
                            "Shipment cancelled at customer request"));
                }
            }

            log.info("Demo data seeded: {} customers, {} drivers, {} shipments, {} status history entries",
                    customerRepo.count(), driverRepo.count(),
                    shipmentRepo.count(), historyRepo.count());
        };
    }

    private Shipment buildShipment(Customer customer, Driver driver, String recipient,
                                    String address, String weight, String status,
                                    LocalDateTime createdAt) {
        Shipment s = new Shipment();
        s.setCustomer(customer);
        s.setDriver(driver);
        s.setRecipientName(recipient);
        s.setDeliveryAddress(address);
        s.setPackageWeight(new BigDecimal(weight));
        s.setCurrentStatus(status);
        s.setCreatedAt(createdAt);
        return s;
    }
}