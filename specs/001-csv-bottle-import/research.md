# Research: CSV Bottle Import and Cellar View

## Decision 1: Backend Stack Uses Spring Boot + Maven + Spring Data MongoDB

- Decision: Use Spring Boot (Java 21) with Maven and Spring Data MongoDB for
  backend APIs and persistence.
- Rationale: Matches requested stack, provides mature dependency management,
  fast API development, robust validation support, and clean layering using
  controller -> application service -> domain -> repository patterns.
- Alternatives considered:
  - Node.js/Express: Rejected because feature explicitly requests Spring Boot.
  - Manual Mongo Java driver without Spring Data: Rejected due to higher boilerplate and weaker repository ergonomics.

## Decision 2: MongoDB Runs in Docker via Compose

- Decision: Use `mongo:8` with Docker Compose for local backend development.
- Rationale: Reproducible environment, simple onboarding, and no host Mongo
  installation requirements.
- Alternatives considered:
  - Local host Mongo install: Rejected due to environment drift risk.
  - Embedded Mongo for development runtime: Rejected for lower parity with production-like behavior.

## Decision 3: CSV Parsing Uses Apache Commons CSV

- Decision: Parse uploaded CSV with Apache Commons CSV using header-based
  mapping and row-level validation.
- Rationale: Stable, widely used parser with predictable behavior, clear error
  handling, and support for quoted values.
- Alternatives considered:
  - OpenCSV: Viable but less preferred for this team because Commons CSV is
    simpler for header-driven parsing in this use case.
  - Custom parser: Rejected for correctness and maintenance risk.

## Decision 4: Import Endpoint Is Multipart API

- Decision: Expose `POST /api/imports/wine-bottles` accepting multipart form-data
  with one CSV file.
- Rationale: Native browser support, straightforward frontend integration with
  Vite/React, and explicit upload semantics.
- Alternatives considered:
  - JSON payload containing CSV text: Rejected due to larger payload overhead,
    awkward UX for file uploads, and weaker filename/content-type validation.

## Decision 5: Persist Canonical Domain Entities with Scoped MVP Deviation

- Decision: Persist canonical `WINE`, `BOTTLE`, `BOTTLE_SIZE`, `COUNTRY`,
  `REGION`, `APPELLATION`, and import event entities. Keep `varietal` as plain
  text on `WINE` for this feature only.
- Rationale: Preserves constitution-driven boundaries while keeping MVP
  implementation tractable.
- Alternatives considered:
  - Full grape normalization (`GRAPE`, `WINE_GRAPE`) now: Rejected due to scope
    increase; documented as explicit follow-up.

## Decision 6: Single Configured Owner, No Authentication Flow

- Decision: Backend loads one owner identity from configuration at startup;
  all operations execute in this owner context.
- Rationale: Matches feature scope and avoids introducing identity/access flows.
- Alternatives considered:
  - Implement login/session now: Rejected as out of scope.
  - Anonymous operations with no actor: Rejected because event trail requires actor identity.

## Decision 7: Frontend Extends Existing Template and Design System

- Decision: Reuse `frontend-template/` as the application frontend and add CSV
  import + bottle list flow while preserving design tokens from
  `design/design-system.json`.
- Rationale: Fastest path to a coherent UI that already matches the project
  visual language.
- Alternatives considered:
  - Build new frontend from scratch: Rejected due to unnecessary duplication.
  - Use generic component library defaults: Rejected because it would violate
    design-system constraints.

## Decision 8: Testing Strategy Uses Layered Coverage + Testcontainers

- Decision: Add backend unit tests for parsing/validation logic, integration
  tests for repositories and APIs using MongoDB Testcontainers, plus frontend
  tests for upload/list user flows.
- Rationale: Aligns with constitution testing requirements while keeping tests
  deterministic and representative of real behavior.
- Alternatives considered:
  - Only manual testing: Rejected due to regression risk.
  - Mock-only backend persistence tests: Rejected because Mongo mapping/index
    behavior needs integration validation.
