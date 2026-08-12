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
````

Current values:

```text
Base Fee: R50.00
Rate per kg: R12.50
```

For example, a package weighing 4 kg would have:

```text
R50.00 + (4 × R12.50) = R100.00
```

The calculation is handled within the shipment service layer.

---

## Shipment Status Tracking

The system supports shipment status updates and maintains a history of status changes.

When a shipment status is updated, the application updates the shipment's current status and records the corresponding status update.

The system also maintains shipment status history for auditing purposes.

---

## Search Functionality

Shipments can be searched using different criteria, including:

* Recipient name
* Shipment status
* Combined search criteria

This allows users to locate shipments without retrieving the entire dataset.

---

## Architecture

The application follows a layered backend architecture:

```text
                 Client
                   │
                   ▼
          REST Controllers
                   │
                   ▼
              DTO Layer
                   │
                   ▼
             Service Layer
                   │
                   ▼
           Repository Layer
                   │
                   ▼
             MySQL Database
```

### Controllers

The controller layer handles incoming HTTP requests and exposes REST endpoints.

Current controllers include:

* `CustomerController`
* `DeliveryController`
* `DriverController`
* `ShipmentController`
* `StatusUpdateController`

### Services

The service layer contains business logic and coordinates operations between controllers and repositories.

Current services include:

* `ShipmentService`
* `StatusUpdateService`

### DTOs

The application uses Data Transfer Objects to control the data exchanged through the API.

* `ShipmentRequestDTO`
* `ShipmentResponseDTO`

### Repositories

The repository layer uses Spring Data JPA for database access.

Repositories include:

* `CustomerRepository`
* `DeliveryRepository`
* `DriverRepository`
* `ShipmentRepository`
* `ShipmentStatusHistoryRepository`
* `StatusUpdateRepository`

### Exception Handling

The application includes centralized exception handling using:

* `GlobalExceptionHandler`
* `ResourceNotFoundException`

---

## Data Models

The application contains models representing:

* Customers
* Drivers
* Shipments
* Deliveries
* Shipment status updates
* Shipment status history

---

## Technologies

| Technology         | Purpose                         |
| ------------------ | ------------------------------- |
| Java               | Programming language            |
| Spring Boot        | Backend framework               |
| Spring Web         | REST API development            |
| Spring Data JPA    | Persistence and database access |
| MySQL              | Relational database             |
| Maven              | Build and dependency management |
| Jakarta Validation | Request validation              |
| Lombok             | Boilerplate reduction           |
| SpringDoc OpenAPI  | API documentation               |
| JUnit 5            | Testing                         |
| Mockito            | Mocking and unit testing        |
| MockMvc            | Controller testing              |

---

## Project Structure

```text
src/
├── main/
│   ├── java/com/example/logistics/
│   │   ├── config/
│   │   ├── controllers/
│   │   ├── dtos/
│   │   ├── exceptions/
│   │   ├── models/
│   │   ├── repositories/
│   │   ├── services/
│   │   └── LogisticsApplication.java
│   │
│   └── resources/
│       ├── static/
│       └── application.properties
│
└── test/
    └── java/com/example/logistics/
        ├── controllers/
        │   └── ShipmentControllerTest.java
        │
        └── services/
            └── StatusUpdateServiceTest.java
```

---

## API Endpoints

### Shipments

| Method | Endpoint                | Description               |
| ------ | ----------------------- | ------------------------- |
| GET    | `/api/shipments`        | Retrieve shipments        |
| GET    | `/api/shipments/{id}`   | Retrieve a shipment by ID |
| GET    | `/api/shipments/search` | Search shipments          |
| POST   | `/api/shipments`        | Create a shipment         |

### Status Updates

The API also supports shipment status updates through the status-update functionality.

---

## Example Request

### Create Shipment

```json
{
  "customer": {
    "customerId": 1
  },
  "driver": {
    "driverId": 1
  },
  "packageWeight": 12.50,
  "recipientName": "Bob Marley",
  "deliveryAddress": "123 Main St, Kimberley",
  "currentStatus": "Pending"
}
```

---

## Testing

The project includes automated tests for both controller and service-layer functionality.

### Controller Testing

`ShipmentControllerTest` uses:

* JUnit 5
* Mockito
* Spring MockMvc

The tests include:

* Valid shipment creation requests
* Invalid shipment creation requests
* HTTP 200 responses
* HTTP 400 validation responses

### Service Testing

`StatusUpdateServiceTest` verifies that:

* A shipment can be located by ID
* A shipment's current status is updated
* The updated shipment is saved
* The status update is saved
* Repository interactions occur as expected

---

## Getting Started

### Prerequisites

Install:

* Java
* MySQL
* Git

Maven is included through the Maven Wrapper.

### Clone the Repository

```bash
git clone https://github.com/chacha-debug/logistics-management-api.git
```

```bash
cd logistics-management-api
```

### Configure the Database

Configure your MySQL database connection in:

```text
src/main/resources/application.properties
```

Do not commit real passwords, API keys, or other sensitive credentials to GitHub.

### Run the Application

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

### Run Tests

Windows:

```bash
mvnw.cmd test
```

macOS/Linux:

```bash
./mvnw test
```

---

## API Documentation

The project includes OpenAPI / Swagger support for documenting and testing the REST API.

After starting the application, open the configured Swagger UI endpoint in your browser.

---

## Future Improvements

Possible future improvements include:

* Authentication and authorization
* Role-based access control
* Pagination
* More advanced shipment filtering
* Improved delivery tracking
* Automated notifications
* Docker containerization
* CI/CD integration
* Additional integration tests
* Production deployment

---

## Author

### Chantele Mucuio

ICT Student | Aspiring Software Engineer | Backend & Full-Stack Developer

GitHub: [chacha-debug](https://github.com/chacha-debug)
