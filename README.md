# Project Name

Ticket Service Application

## Features
- Book tickets for venue Level Orchestra, Main , Balcony 1 and Balcony 2


## Tech Stack
- Java 17
- Spring Boot
- Maven

## Getting Started

### Prerequisites
- JDK 17+
- Maven

### Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

### Application Usage
####1. Get number of seats in the requested level that are neither held nor reserved
```curl --location 'http://<hostname>:<port>/api/tickets/numSeatsAvailable?level=1'```
####2. Find and hold the best available seats for a customer
```
curl --location 'http://<hostname>:<port>/api/tickets/findAndHoldSeats' \
--header 'Content-Type: application/json' \
--data-raw '{
    "numSeats":60,
    "minLevel":1,
    "maxLevel":4,
    "customerEmail":"testuser@gmail.com"
}'
```
####3. Reserve the seats for a specific customer
```
curl --location 'http://<hostname>:<port>/api/tickets/reserveSeats' \
--header 'Content-Type: application/json' \
--data-raw '{
    "seatHoldId":2,
    "customerEmail":"testuser@gmail.com"
}'
```
