# Logistics Management API

A Spring Boot REST API for managing logistics operations, including customers, drivers, shipments, deliveries, shipment status updates, status history auditing, and shipping-fee calculation.

## Overview

The Logistics Management API is a backend application designed to support core logistics operations through RESTful endpoints.

The system manages customers, drivers, shipments, deliveries, and shipment status updates while providing search functionality, validation, status-history auditing, dynamic shipping-fee calculation, and centralized exception handling.

The application follows a layered architecture that separates controllers, services, repositories, DTOs, models, and exception handling.

---

## Features

- Customer management
- Driver management
- Shipment management
- Delivery management
- Shipment search
- Search by recipient name
- Search by shipment status
- Shipment status updates
- Shipment status history and audit logging
- Dynamic shipping-fee calculation
- Request and response DTOs
- Input validation
- Centralized exception handling
- RESTful API endpoints
- MySQL database integration
- OpenAPI / Swagger documentation
- Automated controller and service testing

---

## Shipping Fee Calculation

The application calculates shipping fees based on package weight.

The current calculation uses:

```text
Shipping Fee = Base Fee + (Package Weight × Rate per kg)
