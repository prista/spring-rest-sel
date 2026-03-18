# Technical Specification – Task Scheduler REST Service

## 1. Overview

A simple REST service that allows users to plan and manage tasks.  
The service acts as a lightweight organizer backend.

At the current stage, the application:

- exposes REST endpoints
- stores data in PostgreSQL via JDBC (`spring-boot-starter-jdbc`)
- uses Flyway for database migrations (`db/migration/`)
- uses HTTP Basic authentication with DB-backed user store (Spring Security)
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
- Spring Security (`spring-boot-starter-security`)
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

## 2.2 Security Layer

All endpoints are protected with HTTP Basic authentication via Spring Security.

- `SecurityConfig` — configures stateless session management, disables CSRF, enables HTTP Basic auth, requires authentication on all requests (`authorizeHttpRequests`)
- `ApplicationUser` — Java record implementing `UserDetails`; injected into controller methods via `@AuthenticationPrincipal`
- `ApplicationUserDetailsService` — extends `MappingSqlQuery` and implements `UserDetailsService`, loads users from `t_application_user` table
- Tasks are user-scoped: `t_task.id_application_user` FK links each task to its owner. Users can only list and create their own tasks.

---

## 2.3 Application Layers

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
