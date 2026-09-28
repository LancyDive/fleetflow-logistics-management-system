# FleetFlow Logistics Management System

FleetFlow is a backend-focused logistics and fleet management platform built with Java and Spring Boot. It manages users, vehicles and shipments with role-based access control, JWT authentication and database-backed business rules.

## Current Stack

- Java 17
- Spring Boot 3.5.16
- Spring Security
- JWT (JJWT)
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Jakarta Bean Validation
- JUnit 5 / Spring Boot Test
- Postman for API testing

## Core Features

### Authentication & Authorization
- User registration and login
- BCrypt password hashing
- JWT-based stateless authentication
- Role-based authorization for ADMIN, MANAGER and DRIVER
- Protected API endpoints with HTTP-method and role-specific access rules

### User Management
- View users
- Partial profile updates with PATCH
- Role management for authorized administrators
- Enable/disable user accounts
- Business-level authorization rules for ADMIN and MANAGER actions

### Vehicle Management
- Create, read, update and soft-delete vehicles
- Unique registration and chassis number validation
- Assign drivers to vehicles
- Driver assignment conflict checks
- Vehicle operational states: AVAILABLE, IN_USE, MAINTENANCE and OUT_OF_SERVICE
- Shipment-aware restrictions on driver and status changes

### Shipment Management
- Create and manage shipments
- Unique tracking number generation
- Assign a shipment to a vehicle and its assigned driver
- Capacity validation before assignment
- Shipment lifecycle:
  `CREATED -> ASSIGNED -> PICKED_UP -> IN_TRANSIT -> DELIVERED`
- Vehicle state transitions managed as part of shipment operations
- Soft deletion/deactivation after delivery

### Validation & Error Handling
- Request DTO validation using Jakarta Validation
- Custom domain exceptions
- Centralized `GlobalExceptionHandler`
- Clear HTTP responses for validation, authorization and business-rule failures

### Transaction & Concurrency Handling
- Service-layer transaction boundaries using Spring `@Transactional`
- JPA optimistic locking with `@Version` for shipment and vehicle updates
- Integration coverage for stale shipment updates
- Conflict handling with HTTP 409 responses

## Architecture

The backend follows a layered structure:

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
PostgreSQL

Security
    |
    +--> JWT Authentication Filter
    +--> Custom UserDetailsService
    +--> Role-based authorization
```

Main packages:

```text
controller     -> REST API endpoints
dto            -> request/response models
entity         -> JPA entities
repository     -> Spring Data repositories
service        -> service interfaces
serviceImpl    -> business logic
security       -> JWT and Spring Security
mapper         -> entity/DTO mapping
exception      -> domain exceptions and global handler
constants      -> roles, statuses and enum types
```

## API Areas

Base URL:

```text
http://localhost:8080
```

Authentication:

```text
POST /api/auth/register
POST /api/auth/login
```

Vehicles:

```text
GET    /api/vehicles
GET    /api/vehicles/{id}
POST   /api/vehicles
PATCH  /api/vehicles/{id}
PUT    /api/vehicles/{id}/driver
PUT    /api/vehicles/{id}/status
DELETE /api/vehicles/{id}
```

Shipments:

```text
GET    /api/shipments
GET    /api/shipments/{id}
POST   /api/shipments
PUT    /api/shipments/{id}
PUT    /api/shipments/{id}/assign
PUT    /api/shipments/{id}/pickup
PUT    /api/shipments/{id}/transit
PUT    /api/shipments/{id}/deliver
DELETE /api/shipments/{id}
```

Users:

```text
GET   /api/users
GET   /api/users/{id}
PATCH /api/users/{id}
PUT   /api/users/{id}/role
PUT   /api/users/{id}/status
```

## Running Locally

### Prerequisites

- Java 17
- PostgreSQL
- Maven (or use the included Maven Wrapper)

### 1. Create the database

Create a PostgreSQL database named:

```text
fleetflow
```

### 2. Configure environment variables

Copy the example configuration and provide your local values:

```text
backend/src/main/resources/application-example.properties
```

Set:

```text
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

The real `application.properties` is intentionally excluded from version control.

### 3. Run the application

From the `backend` directory:

Windows:

```powershell
./mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
./mvnw spring-boot:run
```

## Testing

The project includes automated tests for core behavior, including optimistic locking and global exception handling.

Run:

```bash
./mvnw test
```

## Development Notes

- Database persistence uses Spring Data JPA and Hibernate.
- Passwords are hashed with BCrypt.
- JWT authentication is stateless.
- Business rules are enforced in the service layer rather than relying only on controller-level checks.
- Soft deletion is used for users, vehicles and shipments where applicable.

## Project Status

This is an actively developed portfolio project. The backend is being built incrementally with an emphasis on security, business-rule enforcement, database consistency, testing and clean separation of responsibilities.
