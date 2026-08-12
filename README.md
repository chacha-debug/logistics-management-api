# Logistics Management API

A Spring Boot REST API for managing logistics operations, including customers, drivers, shipments, deliveries, shipment status updates, status history auditing, and dynamic shipping-fee calculation.

## Overview

The Logistics Management API is a backend application designed to provide RESTful services for managing core logistics operations.

The system allows users to manage customers, drivers, shipments, and deliveries while providing shipment search, status tracking, status history auditing, validation, exception handling, and automated shipping-fee calculation.

The project was developed using Java and Spring Boot with a layered architecture separating controllers, services, repositories, DTOs, models, and exception handling.

## Features

- Customer management
- Driver management
- Shipment management
- Delivery management
- Shipment search by recipient name and status
- Shipment status updates
- Shipment status history and audit logging
- Dynamic shipping-fee calculation
- Request and response DTOs
- Input validation
- Centralized exception handling
- RESTful API endpoints
- MySQL database integration
- OpenAPI / Swagger API documentation
- Automated testing structure

## Shipping Fee Calculation

The application calculates shipping fees dynamically according to the package weight.

The current calculation is:

**Shipping Fee = Base Fee + (Package Weight × Rate per kg)**

Current values:

- Base fee: `R50.00`
- Rate per kilogram: `R12.50`

For example, a package weighing `4 kg` would have a calculated shipping fee of:

```text
R50.00 + (4 × R12.50)
= R100.00
