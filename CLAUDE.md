# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

We're building the app described in @SPEC.MD. Read that file for the full technical specification,
architecture, and feature list.

Keep replies extremely concise. No unnecessary fluff, no long code snippets.

Whenever working with any third-party library, you MUST look up the official documentation.
Use the DocsExplorer subagent for efficient documentation lookup.

## Commands

- **Build:** `mvn clean package`
- **Run:** `mvn spring-boot:run` (serves on port 8080)

No tests exist yet (no `src/test/` directory or test dependencies).

## Architecture

Java 21 / Spring Boot 3.2 REST API. Single Maven module, single package `drm.sel.showcase`.

- **Flow:** Controller → Service → Repository (in-memory `LinkedList`)
- **API base path:** `/api/tasks`
- **Data classes:** Java records (`Task`, `NewTaskPayload`, `ErrorsPresentation`)
- **Validation:** Manual in controller, i18n errors via `MessageSource`
- **i18n:** `src/main/resources/messages*.properties` (default, `en`, `ru_RU`). UTF-8 encoding required.

## Gotcha

Maven compiler uses `-parameters` flag — required by Spring 6.1+ for parameter name resolution. Without it, `@PathVariable` etc. need explicit name attributes.
