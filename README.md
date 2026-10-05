# Event Booking Backend

A fresher-level event booking backend built with **Java, Spring Boot, Spring JDBC, and PostgreSQL**.

The system models users, venues, physical venue seats, events, event-specific seats, bookings, and booking items. The main engineering focus is the **Spring Boot backend, relational database design, transactions, and concurrent seat booking**.

A small React/Vite frontend is included as a client of the REST APIs. It is intentionally secondary to the backend.

> **Scope:** This is a learning/fresher-level project, not a production-ready booking platform. Authentication, payments, temporary seat holds, and other production concerns are intentionally outside the current scope.

---

## Table of Contents

- [Overview](#overview)
- [Goals](#goals)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [Core Design Decisions](#core-design-decisions)
- [Application Flows](#application-flows)
- [REST API](#rest-api)
- [Concurrency and Double Booking](#concurrency-and-double-booking)
- [Transactions](#transactions)
- [Validation and Error Handling](#validation-and-error-handling)
- [CORS and Frontend](#cors-and-frontend)
- [Running the Project](#running-the-project)
- [Testing with curl](#testing-with-curl)
- [Current Limitations](#current-limitations)
- [Future Improvements](#future-improvements)
- [Interview Talking Points](#interview-talking-points)
- [Acknowledgement](#acknowledgement)

---

# Overview

The Event Booking application provides REST APIs for:

- Creating users
- Creating venues
- Automatically generating physical venue seats
- Creating events
- Automatically generating event-specific seats
- Viewing events
- Viewing event seats
- Booking available seats

The most important operation is **seat booking**.

A venue owns physical seats:

```text
Venue
 ├── S1
 ├── S2
 ├── S3
 └── ...
```

When an event is created at that venue, the system creates event-specific seat records:

```text
Event
 ├── EventSeat S1 → AVAILABLE
 ├── EventSeat S2 → AVAILABLE
 ├── EventSeat S3 → AVAILABLE
 └── ...
```

This allows the same physical venue to be reused for many events without sharing booking state between them.

---

# Goals

The project was built to learn and demonstrate:

- Spring Boot
- REST APIs
- Controller / Service / Repository separation
- DTOs
- Constructor-based dependency injection
- Spring JDBC / `JdbcTemplate`
- PostgreSQL
- Foreign keys and relational modeling
- Transactions
- Database-backed concurrency control
- Conditional SQL updates
- Request validation
- Basic exception handling
- Connecting a React frontend to a Spring Boot backend

The project deliberately avoids unnecessary production-level complexity.

---

# Tech Stack

## Backend

- Java
- Spring Boot
- Spring MVC
- Spring JDBC
- `JdbcTemplate`
- Maven
- PostgreSQL
- Jakarta Validation

## Frontend

- React
- Vite
- JavaScript
- CSS

The frontend exists primarily to demonstrate consumption of the backend REST APIs.

---

# Architecture

The backend follows a simple layered architecture:

```text
HTTP Request
     │
     ▼
┌──────────────┐
│  Controller  │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│    DTO       │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   Service    │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Repository  │
└──────┬───────┘
       │
       │ JdbcTemplate
       ▼
┌──────────────┐
│ PostgreSQL   │
└──────────────┘
```

### Controller

Handles HTTP concerns:

- Routes
- Request bodies
- Validation
- HTTP responses

### DTO

Represents API input such as:

- `UserRequest`
- `EventRequest`
- `BookingRequest`

### Service

Contains application/business logic and coordinates repositories.

### Repository

Handles PostgreSQL access through `JdbcTemplate` and explicit SQL.

### PostgreSQL

Acts as the source of truth for persistent data and seat state.

---

# Project Structure

The backend is organized approximately as:

```text
src/main/java/com/example/event_booking/
├── controller/
│   ├── BookingController.java
│   ├── EventController.java
│   ├── UserController.java
│   └── VenueController.java
│
├── dto/
│   ├── BookingRequest.java
│   ├── EventRequest.java
│   └── UserRequest.java
│
├── model/
│   ├── Event.java
│   ├── EventSeat.java
│   ├── User.java
│   ├── Venue.java
│   └── VenueSeat.java
│
├── repository/
│   ├── BookingRepository.java
│   ├── EventRepository.java
│   ├── EventSeatRepository.java
│   ├── UserRepository.java
│   ├── VenueRepository.java
│   └── VenueSeatRepository.java
│
├── service/
│   ├── BookingService.java
│   ├── EventService.java
│   ├── UserService.java
│   └── VenueService.java
│
└── EventBookingApplication.java
```

---

# Database Design

The main tables are:

```text
users
venues
venue_seats
events
event_seats
bookings
booking_items
```

## Users

```text
id
name
email
created_at
```

Authentication is intentionally not implemented.

## Venues

```text
id
name
address
capacity
```

When a venue is created, its capacity determines how many physical seats are generated.

## Venue Seats

Represents physical seats belonging to a venue:

```text
id | venue_id | seat_number
---+----------+------------
12 | 4        | S1
13 | 4        | S2
14 | 4        | S3
15 | 4        | S4
16 | 4        | S5
```

There is intentionally no public VenueSeat CRUD API. They are generated automatically when a venue is created.

## Events

```text
id
name
venue_id
start_time
end_time
description
status
created_at
```

Each event belongs to one venue.

## Event Seats

`event_seats` represents the state of a physical venue seat **for one particular event**.

```text
id
event_id
seat_id
status
held_by_user_id
hold_expires_at
price
```

Example:

```text
id | event_id | seat_id | status
---+----------+---------+---------
16 | 4        | 12      | BOOKED
17 | 4        | 13      | AVAILABLE
18 | 4        | 14      | AVAILABLE
```

Important distinction:

- `seat_id` identifies the physical `venue_seats` row.
- `event_seats.id` identifies the event-specific seat row.
- The booking API uses the **event seat ID**.

## Bookings

```text
id
user_id
event_id
status
created_at
```

The current implementation creates bookings with:

```text
status = CONFIRMED
```

## Booking Items

```text
id
booking_id
event_seat_id
price
```

---

# Core Design Decisions

## VenueSeat vs EventSeat

A physical seat belongs to a venue, but its availability is specific to an event.

```text
Venue 4
 └── Physical S1

Event A
 └── S1 → BOOKED

Event B
 └── S1 → AVAILABLE
```

Booking S1 for Event A does not book it for Event B.

## Why no public VenueSeat API?

Venue seats are an internal resource generated from venue capacity:

```text
Venue(capacity = 5)
        ↓
S1 S2 S3 S4 S5
```

The frontend does not need to manually create them.

## Why Spring JDBC?

The project uses Spring JDBC instead of JPA/Hibernate so SQL and database behavior remain explicit.

---

# Application Flows

## Creating a Venue

```text
POST /venues
      │
      ▼
VenueController
      │
      ▼
VenueService
      │
      ├── Insert Venue
      │
      └── Insert S1...Sn
              │
              ▼
          PostgreSQL
```

The operation is transactional.

## Creating an Event

Example:

```json
{
  "name": "Test Event",
  "price": 599,
  "venueId": 4,
  "startTime": "2026-10-10T19:00:00",
  "endTime": "2026-10-10T22:00:00",
  "description": "Testing event creation"
}
```

Flow:

```text
POST /events
      │
      ▼
EventController
      │
      ▼
EventService
      │
      ├── Create Event
      ├── Find Venue Seats
      └── Create EventSeat for every VenueSeat
                  │
                  ▼
              PostgreSQL
```

## Booking a Seat

Example:

```json
{
  "eventId": 4,
  "eventSeatId": 16
}
```

Flow:

```text
BookingController
       │
       ▼
BookingService
       │
       ▼
EventSeatRepository
       │
       ▼
PostgreSQL
       │
       ├── AVAILABLE → BOOKED
       │
       ▼
BookingRepository
       ├── Create Booking
       └── Create BookingItem
```

---

# Concurrency and Double Booking

This is the main backend design problem.

A naive implementation could do:

```text
SELECT status
       ↓
if AVAILABLE
       ↓
UPDATE to BOOKED
```

Two concurrent requests could both read `AVAILABLE`.

Instead, the project uses:

```sql
UPDATE event_seats
SET status = 'BOOKED'
WHERE id = ?
AND event_id = ?
AND status = 'AVAILABLE';
```

### First request

```text
AVAILABLE
    ↓
UPDATE matches row
    ↓
BOOKED
```

Affected rows:

```text
1
```

### Second request

The seat is already `BOOKED`, so:

```text
status = 'AVAILABLE'
```

is false.

Affected rows:

```text
0
```

The service returns:

```text
Seat is not available
```

with HTTP 409.

The important concurrency control is the **conditional database update**.

---

# Why Java Alone Is Not Enough

A Java check such as:

```java
if (seatIsAvailable) {
    bookSeat();
}
```

is not enough because multiple application threads can execute it concurrently.

The database is the shared source of truth.

The application checks the result:

```java
int rows = eventSeatRepository.bookSeat(...);

if (rows == 0) {
    return false;
}
```

The database enforces the critical state transition.

---

# Transactions

The booking method uses:

```java
@Transactional
```

The logical operation is:

```text
1. Mark seat BOOKED
2. Create Booking
3. Create BookingItem
```

Conceptually:

```text
BEGIN

AVAILABLE → BOOKED

INSERT booking

INSERT booking_item

COMMIT
```

If a later operation fails with a rollback-triggering exception, earlier changes can be rolled back.

---

# Seat Holding

The database contains:

```text
held_by_user_id
hold_expires_at
```

but the current application **does not implement temporary seat holds**.

There is no current:

```text
AVAILABLE → HELD
```

transition.

The current flow is:

```text
AVAILABLE
    ↓
Booking request
    ↓
BOOKED
```

A future system could implement:

```text
AVAILABLE
    ↓
HELD
    ├── payment succeeds → BOOKED
    └── timeout → AVAILABLE
```

This was intentionally excluded from the current fresher-level scope.

---

# REST API

## Users

```http
POST /users
```

Example:

```json
{
  "name": "Rounak",
  "email": "rounak@example.com"
}
```

## Venues

```http
POST /venues
```

Example:

```json
{
  "name": "Main Auditorium",
  "address": "Campus",
  "capacity": 5
}
```

## Events

Create:

```http
POST /events
```

Get all:

```http
GET /events
```

Get one:

```http
GET /events/{eventId}
```

Get seats:

```http
GET /events/{eventId}/seats
```

Example event creation:

```json
{
  "name": "Test Event",
  "price": 599,
  "venueId": 4,
  "startTime": "2026-10-10T19:00:00",
  "endTime": "2026-10-10T22:00:00",
  "description": "Testing event creation"
}
```

## Bookings

```http
POST /bookings
```

Example:

```json
{
  "eventId": 4,
  "eventSeatId": 16
}
```

Success:

```text
201 Created
Booking successful
```

Already booked:

```text
409 Conflict
Seat is not available
```

---

# Validation and Error Handling

The project uses Jakarta Validation annotations such as:

```java
@NotBlank
@NotNull
@Positive
@Email
```

Controllers use:

```java
@Valid @RequestBody
```

A global exception handler handles common validation and database integrity failures.

The current implementation is intentionally simple.

---

# CORS and Frontend

The React frontend runs on:

```text
http://localhost:5173
```

Spring Boot runs on:

```text
http://localhost:8080
```

Because the ports differ, they are different origins.

The backend allows the React development origin with:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

This allows browser JavaScript from React to call the backend.

CORS is **not an API gateway**. It is a browser security mechanism controlling cross-origin access.

The frontend flow is:

```text
React/Vite
     │
     │ HTTP + JSON
     ▼
Spring Boot REST API
     │
     ▼
PostgreSQL
```

React does not connect directly to PostgreSQL or implement booking concurrency.

---

# Running the Project

## Prerequisites

- Java
- PostgreSQL
- Node.js / npm

The backend uses the Maven wrapper.

## Backend

```cmd
cd C:\Users\aryan\OneDrive\Desktop\event-booking
mvnw.cmd spring-boot:run
```

Backend:

```text
http://localhost:8080
```

## Frontend

```cmd
cd C:\Users\aryan\Downloads\event-booking-frontend\event-booking-frontend
npm install
npm run dev
```

Frontend normally:

```text
http://localhost:5173/
```

Vite may choose another port if 5173 is occupied.

---

# Testing with curl

Get events:

```cmd
curl.exe http://localhost:8080/events
```

Get one event:

```cmd
curl.exe http://localhost:8080/events/4
```

Get event seats:

```cmd
curl.exe http://localhost:8080/events/4/seats
```

Book a seat:

```cmd
curl.exe -X POST http://localhost:8080/bookings -H "Content-Type: application/json" -d "{\"eventId\":4,\"eventSeatId\":16}"
```

A successful request returns:

```text
Booking successful
```

Trying the same event seat again should return:

```text
Seat is not available
```

with HTTP 409.

---

# Current Limitations

This project intentionally does not attempt to be production-ready.

- **Authentication:** not implemented; booking temporarily uses user ID 1.
- **Authorization:** no roles or permissions.
- **Payments:** no payment integration.
- **Seat holds:** not implemented.
- **Multiple seats per booking:** current endpoint handles one event seat per booking request.
- **Event business rules:** additional validation such as `endTime > startTime` can be added.
- **Error responses:** some database errors could be mapped more precisely.
- **Deployment:** local development only.

The project does not currently include Docker/Kubernetes, Redis, Kafka, microservices, distributed locks, CI/CD, or cloud deployment. These are deliberately outside the current scope.

---

# Future Improvements

Possible future additions:

1. Authentication and authorization
2. User-specific bookings
3. Multiple seats per booking
4. Temporary seat holds
5. Payment processing
6. Structured error responses
7. Event update/delete APIs
8. Venue management APIs
9. More automated tests
10. Production deployment

These are future extensions, not requirements for the current fresher-level version.

---

# Interview Talking Points

### Why Spring Boot?

It simplifies REST API development and provides dependency injection, validation, embedded server support, and Spring ecosystem integration.

### Why Controller / Service / Repository?

```text
Controller → HTTP
Service → Application/business logic
Repository → Database
```

### Why DTOs?

DTOs represent API input separately from internal domain models.

### Why Spring JDBC?

It keeps SQL explicit and makes database behavior easy to reason about.

### Why `event_seats`?

Physical venue seats and event-specific availability are different concepts.

### How is double booking prevented?

The database executes:

```sql
UPDATE ...
WHERE status = 'AVAILABLE'
```

and the application checks affected rows.

### Why isn't a Java availability check enough?

Multiple application threads can read the same state concurrently. PostgreSQL is the shared source of truth.

### Why `@Transactional`?

Booking contains multiple related database operations that should succeed or fail as one logical unit.

### What happens if two users book the same seat?

One request changes the seat to `BOOKED`. The other conditional update affects zero rows and receives a 409 conflict.

### Why isn't HOLD implemented?

The current project intentionally keeps booking simple. Hold-related columns exist in the schema, but temporary holds are outside the current implementation.

---

# Project Scope Philosophy

The project follows:

> **Build something small enough to understand completely.**

The important backend flow is:

```text
HTTP
 ↓
Spring Boot Controller
 ↓
DTO
 ↓
Service
 ↓
Repository
 ↓
JdbcTemplate
 ↓
SQL
 ↓
PostgreSQL
```

The project focuses on understanding REST APIs, dependency injection, database modeling, transactions, concurrency, conditional updates, validation, and PostgreSQL relationships rather than adding technologies purely to make the project larger.

For a fresher-level SDE project, a smaller system that can be explained deeply is more valuable than a production-sized system whose internals are not understood.

---

# Author

**Rounak Aryan**

Computer Science student with a focus on backend/SDE development.

Primary technologies demonstrated:

- Java
- Spring Boot
- Spring JDBC
- PostgreSQL
- REST APIs
- React/Vite

---

# Acknowledgement

This project and its documentation were developed with assistance from **OpenAI ChatGPT**.

ChatGPT was used as a development and learning assistant for:

- discussing architecture
- reasoning about the database schema
- explaining Spring Boot concepts
- debugging implementation issues
- reasoning about concurrent seat booking
- testing API flows
- connecting the React frontend to the backend
- preparing this documentation

The project was developed iteratively, with implementation and testing performed during development. The main goal was to understand the backend flow and the reasoning behind the design rather than treating generated code as a black box.
