# Ticket Booking System - Spring Boot with Redis & PostgreSQL

A fully functional ticket booking system with concurrent booking support using Redis distributed locks and optimistic locking.

## Architecture Overview

### How Redis and PostgreSQL Work Together

**PostgreSQL Database:**
- Stores persistent data: Shows, Seats, Bookings
- Uses optimistic locking (version field) to handle concurrent seat bookings
- Ensures ACID properties for data integrity

**Redis Cache:**
- Acts as a distributed lock manager for seat booking
- Prevents race conditions between concurrent booking requests
- Provides fast, atomic lock acquisition and release
- Automatically expires locks after 10 seconds if not released

### Booking Flow

```
User Request
    ↓
[Redis Lock Service] ← Acquires distributed lock for seat
    ↓
[Seat Service] ← Checks seat availability in PostgreSQL
    ↓
[Booking Execution] ← Updates seat status: AVAILABLE → LOCKED → BOOKED
    ↓
[Booking Repository] ← Creates booking record in PostgreSQL
    ↓
[Redis Lock Release] ← Releases lock after transaction completes
    ↓
Response to User
```

## Prerequisites

- **Java 21+** (tested with Java 21 and 25)
- **PostgreSQL 12+** (local or remote)
- **Redis 6+** (local for development or cloud Redis)
- **Maven 3.9+**

## Setup Instructions

### 1. PostgreSQL Setup

```bash
# Create database
createdb finalproject

# Or use psql:
psql -U postgres
CREATE DATABASE finalproject;
```

### 2. Redis Setup

**Option A: Local Redis (Windows/Linux)**
```bash
# Download and install Redis from https://redis.io/download
redis-server  # Start Redis on port 6379
```

**Option B: Cloud Redis (Redis Cloud/AWS ElastiCache)**
Update `application.properties`:
```properties
spring.data.redis.host=your-redis-host.com
spring.data.redis.port=6379
spring.data.redis.password=your-password
spring.data.redis.ssl=true
```

### 3. Configure Database Connection

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finalproject
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### 4. Build and Run

```bash
# Build the project
mvn clean package

# Run the application
java -jar target/finalprojectspringboot-0.0.1-SNAPSHOT.jar

# Or run with Maven
mvn spring-boot:run
```

Application starts on: **http://localhost:8080**

## API Endpoints

### Show Management

#### 1. Create a Show
```bash
POST /api/shows
Content-Type: application/json

{
  "movieName": "Avengers: Endgame",
  "showTime": "2026-04-22T15:30:00",
  "totalSeats": 80
}

Response: 201 Created
{
  "id": 1,
  "movieName": "Avengers: Endgame",
  "showTime": "2026-04-22T15:30:00",
  "totalSeats": 80,
  "availableSeats": 80,
  "createdAt": "2026-04-22T12:00:00",
  "updatedAt": "2026-04-22T12:00:00"
}
```

#### 2. Get All Shows
```bash
GET /api/shows

Response: 200 OK
[
  {
    "id": 1,
    "movieName": "Avengers: Endgame",
    "showTime": "2026-04-22T15:30:00",
    "totalSeats": 80,
    "availableSeats": 79,
    ...
  }
]
```

#### 3. Get Specific Show
```bash
GET /api/shows/{showId}

Response: 200 OK
{
  "id": 1,
  "movieName": "Avengers: Endgame",
  ...
}
```

### Seat Management

#### 4. Get All Seats for a Show
```bash
GET /api/seats/{showId}

Response: 200 OK
[
  {
    "id": 1,
    "seatNumber": "A1",
    "showId": 1,
    "status": "AVAILABLE",
    "version": 0,
    "lockedAt": null
  },
  {
    "id": 2,
    "seatNumber": "A2",
    "showId": 1,
    "status": "BOOKED",
    "version": 1,
    "lockedAt": null
  }
]
```

### Booking Management

#### 5. Book a Seat (Query Parameters)
```bash
POST /api/book?seatId=1&userId=100

Response: 201 Created
{
  "bookingId": 1,
  "seatId": 1,
  "showId": 1,
  "userId": 100,
  "status": "CONFIRMED",
  "bookingTime": "2026-04-22T12:05:00",
  "message": "Seat booked successfully."
}
```

#### 6. Book a Seat (Request Body)
```bash
POST /api/book/json
Content-Type: application/json

{
  "seatId": 2,
  "userId": 101
}

Response: 201 Created
{
  "bookingId": 2,
  "seatId": 2,
  "showId": 1,
  "userId": 101,
  "status": "CONFIRMED",
  "bookingTime": "2026-04-22T12:06:00",
  "message": "Seat booked successfully."
}
```

#### 7. Simulate Concurrent Booking (Testing)
```bash
POST /api/book/simulate?seatId=1

Response: 200 OK
[
  {
    "bookingId": 3,
    "seatId": 1,
    "showId": 1,
    "userId": 1,
    "status": "CONFIRMED",
    "bookingTime": "2026-04-22T12:07:00",
    "message": "Seat booked successfully."
  },
  {
    "seatId": 1,
    "showId": 1,
    "userId": 2,
    "status": "FAILED",
    "bookingTime": "2026-04-22T12:07:00",
    "message": "Seat 1 is not available for booking."
  },
  ...
]
```

## Sample Usage Workflow

### Step 1: Create a Show
```bash
curl -X POST http://localhost:8080/api/shows \
  -H "Content-Type: application/json" \
  -d '{
    "movieName": "Inception",
    "showTime": "2026-04-25T18:00:00",
    "totalSeats": 80
  }'
```

**Response:** Show ID = 1

### Step 2: View Available Seats
```bash
curl http://localhost:8080/api/seats/1
```

### Step 3: Book a Seat
```bash
curl -X POST "http://localhost:8080/api/book?seatId=1&userId=100"
```

### Step 4: Verify Seat is Booked
```bash
curl http://localhost:8080/api/seats/1
```

### Step 5: Try Concurrent Booking (Stress Test)
```bash
curl -X POST "http://localhost:8080/api/book/simulate?seatId=2"
```

This simulates 50 concurrent users trying to book the same seat. Only 1 will succeed!

## Concurrency Control Mechanisms

### 1. Redis Distributed Locks
- **Location:** `RedisLockService.java`
- **Purpose:** Prevents multiple servers from processing the same seat booking simultaneously
- **Expiration:** 10 seconds (automatic release)
- **Lock Key Format:** `seat_lock_{seatId}`

### 2. Optimistic Locking (JPA @Version)
- **Location:** `Seat.java` entity
- **Purpose:** Detects conflicts when concurrent requests modify the same seat
- **Retry Logic:** 3 attempts with 200ms backoff in `BookingService.java`

### 3. Database Transactions
- **Isolation Level:** READ_COMMITTED (default PostgreSQL)
- **Transactional Methods:** All booking operations wrapped with `@Transactional`

## Key Features

✅ **Distributed Locking** - Redis-based seat locks  
✅ **Optimistic Locking** - JPA version control  
✅ **Concurrent Booking Simulation** - Test with 50 concurrent users  
✅ **Automatic Seat Expiration** - Locked seats auto-release after 2 minutes  
✅ **Request Validation** - Input validation with Jakarta annotations  
✅ **Global Exception Handling** - Centralized error responses  
✅ **Logging** - Java Logger integration  
✅ **Transaction Management** - ACID guarantees  

## Database Schema

### shows table
```sql
CREATE TABLE shows (
    id BIGSERIAL PRIMARY KEY,
    movie_name VARCHAR(100) NOT NULL,
    show_time TIMESTAMP NOT NULL,
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### seats table
```sql
CREATE TABLE seats (
    id BIGSERIAL PRIMARY KEY,
    seat_number VARCHAR(20) NOT NULL,
    show_id BIGINT NOT NULL REFERENCES shows(id),
    status VARCHAR(20) NOT NULL,
    version INTEGER NOT NULL,
    locked_at TIMESTAMP
);
```

### bookings table
```sql
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL REFERENCES seats(id),
    show_id BIGINT NOT NULL REFERENCES shows(id),
    status VARCHAR(20) NOT NULL,
    booking_time TIMESTAMP NOT NULL
);
```

## Troubleshooting

### Redis Connection Error
**Error:** "Redis is unreachable"  
**Solution:** 
- Ensure Redis is running: `redis-cli ping` (should return "PONG")
- Check host/port in `application.properties`
- For cloud Redis, verify network access and credentials

### Database Connection Error
**Error:** "Connection to localhost:5432 refused"  
**Solution:**
- Ensure PostgreSQL is running: `psql -U postgres`
- Verify credentials in `application.properties`
- Check database exists: `psql -l`

### Seat Already Booked
**Error:** "Seat X is not available for booking"  
**Solution:** This is expected when seat is already booked. The system successfully prevented double-booking using Redis locks!

### Concurrency Test Showing All Failures
**Possible Issue:** Booking timeout during simulation  
**Solution:** Increase timeout in `application.properties`:
```properties
spring.data.redis.timeout=5000ms
```

## Project Structure

```
src/main/java/com/example/finalprojectspringboot/
├── config/          # Redis & Spring configurations
├── controller/      # REST API endpoints
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities (Seat, Booking, Show)
├── exception/      # Custom exceptions & global error handling
├── repository/     # JPA repositories
└── service/        # Business logic
    ├── BookingService.java              # High-level booking with retries
    ├── BookingExecutionService.java     # Actual booking logic with locks
    ├── RedisLockService.java            # Distributed lock management
    ├── SeatService.java                 # Seat queries & expiration
    ├── ShowService.java                 # Show management
    └── DataInitializer.java             # Sample data on startup
```

## Performance Characteristics

- **Avg Booking Time:** 2-3 seconds (includes 2s simulation delay)
- **Lock Acquisition:** <10ms
- **Concurrent Users:** Successfully handles 50+ simultaneous bookings
- **Database Queries per Booking:** 4-5

## Technology Stack

- **Framework:** Spring Boot 3.3.5
- **Language:** Java 21
- **Database:** PostgreSQL 12+
- **Cache:** Redis 6+
- **Build Tool:** Maven 3.9+
- **ORM:** Hibernate/JPA
- **Validation:** Jakarta Bean Validation
- **Logging:** Java Util Logger

## License

This project is open source and available for educational purposes.

