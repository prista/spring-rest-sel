# Technical Specification – Task Scheduler REST Service

## 1. Overview

A simple REST service that allows users to plan and manage tasks.  
The service acts as a lightweight organizer backend.

At the current stage, the application:

- exposes REST endpoints
- stores data in PostgreSQL via JDBC (`spring-boot-starter-jdbc`)
- uses Flyway for database migrations (`db/migration/`)
- does not include authentication or security
- does not include caching

### Core Features

- Create tasks
- List tasks
- View a single task
- Update tasks
- Delete tasks
- Mark tasks as completed

### Tech Stack

- Java 21
- Spring Boot 3.5
- Maven
- Spring Web (`spring-boot-starter-web`)
- Spring JDBC (`spring-boot-starter-jdbc`)
- PostgreSQL
- Flyway (`flyway-core`, `flyway-database-postgresql`)
- Jackson (JSON serialization)
- Testcontainers + `spring-boot-testcontainers` (integration tests)


---

# 2. Architecture

## 2.1 High-Level Architecture

The application follows a layered architecture.

Controller layer handles HTTP requests and responses.

Service layer contains business logic.

Repository layer persists tasks in PostgreSQL via `JdbcOperations`.

Flow:

Controller → Service → Repository

---

## 2.2 Application Layers

### Presentation Layer

Handles HTTP requests and responses.

Responsibilities:

- expose REST endpoints
- validate incoming requests
- return HTTP responses

Implemented using:

- `@RestController`
- `@RequestMapping`

Example package:
