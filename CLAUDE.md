# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

We're building the app described in @SPEC.MD. Read that file for the full technical specification,
architecture, and feature list.

Keep replies extremely concise. No unnecessary fluff, no long code snippets.

Whenever working with any third-party library, you MUST look up the official documentation.
Use the DocsExplorer subagent for efficient documentation lookup.

## Commands

- **Build:** `./mvnw clean package`
- **Run:** `./mvnw spring-boot:run` (serves on port 8080)
- **Test (IT only):** `./mvnw test -Dtest="TasksRestControllerIT"`

## Architecture

Java 21 / Spring Boot 3.5 REST API. Single Maven module, single package `drm.sel.showcase`.

- **Flow:** Controller → Repository (`JdbcOperations` → PostgreSQL)
- **API base path:** `/api/tasks`
- **Data classes:** Java records (`Task`, `NewTaskPayload`, `ErrorsPresentation`)
- **DB migrations:** Flyway, `src/main/resources/db/migration/`
- **Validation:** Manual in controller, i18n errors via `MessageSource`
- **i18n:** `src/main/resources/messages*.properties` (default, `en`, `ru_RU`). UTF-8 encoding required.
- **Integration tests:** Testcontainers (PostgreSQL 17.4-alpine) + `@ServiceConnection` via shared `TestcontainersConfiguration`. Test data loaded with `@Sql`.

## Gotcha

Maven compiler uses `-parameters` flag — required by Spring 6.1+ for parameter name resolution. Without it, `@PathVariable` etc. need explicit name attributes.
