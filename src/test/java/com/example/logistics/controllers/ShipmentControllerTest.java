package com.example.logistics.controllers;

import com.example.logistics.models.Customer;
import com.example.logistics.models.Driver;
import com.example.logistics.models.Shipment;
import com.example.logistics.repositories.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ShipmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentController shipmentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shipmentController).build();
    }

    @Test
    void createShipment_ValidPayload_Returns200OK() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Driver driver = new Driver();
        driver.setDriverId(1L);

        Shipment shipment = new Shipment();
        shipment.setCustomer(customer);
        shipment.setDriver(driver);
        shipment.setPackageWeight(new BigDecimal("12.50"));
        shipment.setRecipientName("Bob Marley");
        shipment.setDeliveryAddress("123 Main St, Kimberley");
        shipment.setCurrentStatus("Pending");

        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);

        String validJsonPayload = """
            {
              "customer": { "customerId": 1 },
              "driver": { "driverId": 1 },
              "packageWeight": 12.50,
              "recipientName": "Bob Marley",
              "deliveryAddress": "123 Main St, Kimberley",
              "currentStatus": "Pending"
            }
            """;

        mockMvc.perform(post("/api/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJsonPayload))
                .andExpect(status().isOk());
    }

    @Test
    void createShipment_InvalidPayload_Returns400BadRequest() throws Exception {
        String invalidJsonPayload = """
            {
              "packageWeight": -5.00,
              "recipientName": "",
              "deliveryAddress": ""
            }
            """;

        mockMvc.perform(post("/api/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonPayload))
                .andExpect(status().isBadRequest());
    }
}