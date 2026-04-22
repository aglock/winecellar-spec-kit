# Implementation Plan: CSV Bottle Import and Cellar View

**Branch**: `001-csv-bottle-import` | **Date**: 2026-04-21 | **Spec**: `specs/001-csv-bottle-import/spec.md`
**Input**: Feature specification from `specs/001-csv-bottle-import/spec.md`

## Summary

Build a minimal full-stack web application that allows a single configured
owner user to upload a wine-bottle CSV, persist parsed data in MongoDB, and
view imported bottles immediately in a structured cellar list. The backend uses
Spring Boot + Maven, MongoDB runs in Docker, and the frontend is based on the
existing React/Vite/Tailwind template styled via `design/design-system.json`.

## Technical Context

**Backend Language/Version**: Java 21 + Spring Boot 3.3.x (Maven build)  
**Frontend Language/Version**: React 18, Vite 5, Tailwind CSS 3.4  
**Primary Dependencies**: spring-boot-starter-web, spring-boot-starter-validation, spring-boot-starter-data-mongodb, Apache Commons CSV (or equivalent), React Router 6  
**Storage**: MongoDB 8 in Docker (`mongo:8`)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc/WebTestClient for backend API tests; repository integration tests against MongoDB Testcontainers; Vitest + React Testing Library for frontend component and flow tests  
**Target Platform**: Web application (desktop + mobile responsive)  
**Project Type**: Full-stack application (backend + frontend)  
**UI Design Source**: `design/design-system.json` is authoritative for visual tokens, components, and UX tone  
**Data Modeling Source**: `docs/architecture/information-model.mmd` and `docs/architecture/data-model.md` govern semantic model boundaries  
**Performance Goals**: Import and persist 500 CSV rows in <= 30s; list endpoint p95 <= 500ms for 500 bottles; initial list render <= 2s on a typical laptop browser  
**Constraints**: Single configured owner identity at startup; no auth flow; canonical domain entities and event trail required; keep frontend terminology aligned with constitution  
**Scale/Scope**: Minimal MVP for CSV import + bottle list only; no sharing UI, no membership management UI, no advanced search/filtering

## Constitution Check (Pre-Research)

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Information model first: PASS. Entities and cardinalities are explicit in spec
  and aligned to canonical model.
- Collaboration and access control: PASS with scoped simplification. A single
  configured owner is explicit for this feature; full membership flows are
  intentionally deferred.
- Domain boundary separation: PASS. `WINE` master data, `BOTTLE` inventory,
  `CELLAR`/`COMPARTMENT` structure, and import `EVENT` are separated.
- Canonical concepts and geography: PASS. `COUNTRY`/`REGION`/`APPELLATION`
  semantics maintained.
- Grape master data source alignment: PASS with documented scoped deviation for
  plain-text varietal during MVP import.
- Evented audit trail: PASS. Each import emits one cellar-targeted event with
  actor/time/type/context.
- Nullable relationships justified: PASS. Spec includes explicit nullable
  justifications.
- Code quality and maintainability expectations: PASS. Feature-organized
  modules, thin controllers, service/domain boundaries, explicit DTOs.
- Testing strategy: PASS. Unit + integration + API + frontend behavior tests.
- UX consistency: PASS. Design system and terminology constraints are explicit.
- Performance constraints measurable: PASS. Feature-specific measurable targets
  captured above.

## Project Structure

### Documentation (this feature)

```text
specs/001-csv-bottle-import/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── csv-import-api.yaml
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── pom.xml
├── src/main/java/com/winecellar/importer/
│   ├── application/
│   ├── domain/
│   ├── infrastructure/
│   ├── api/
│   └── config/
├── src/main/resources/
│   ├── application.yml
│   └── application-dev.yml
└── src/test/java/com/winecellar/importer/

frontend-template/
├── src/
│   ├── components/
│   ├── pages/
│   ├── services/
│   └── styles/
└── tests/

docker/
└── mongo-compose.yml
```

**Structure Decision**: Use a dedicated `backend/` Maven Spring Boot service and
reuse `frontend-template/` as the frontend application base. Add Docker Compose
under `docker/` for local MongoDB. This minimizes setup churn and keeps frontend
design-system work anchored to the provided template.

**Phase Test Gate**:

- End of Phase 0: no code tests; verify research decisions resolve all
  technical unknowns.
- End of Phase 1: validate contracts and data model consistency with spec and
  constitution.
- End of implementation phase: run backend unit/integration tests, frontend
  tests, and critical happy-path manual check (upload + list).

## Phase 0: Research And Decisions

Research output is documented in `specs/001-csv-bottle-import/research.md` and
covers:

- Spring Boot + MongoDB integration approach
- CSV parsing library and failure-reporting strategy
- Dockerized MongoDB local environment
- API contract shape for upload/list endpoints
- Frontend integration pattern with existing template

All prior unknowns in technical context are resolved.

## Phase 1: Design And Contracts

Design outputs are documented in:

- `specs/001-csv-bottle-import/data-model.md`
- `specs/001-csv-bottle-import/contracts/csv-import-api.yaml`
- `specs/001-csv-bottle-import/quickstart.md`

Design scope includes:

- MongoDB document collections and indexes for `WINE`, `BOTTLE`, geography,
  and import events
- Request/response contracts for CSV upload and bottle listing
- Startup configuration for a single configured owner identity
- Mapping rules from CSV columns to canonical entities

## Constitution Check (Post-Design)

- Information-model fidelity: PASS. Data model keeps canonical entity
  boundaries and cardinality semantics.
- Event model requirements: PASS. Import event has explicit actor/type/time,
  cellar context, and single primary target.
- Canonical geography: PASS. `APPELLATION` optional, fallback to `REGION`.
- UX/testing/performance obligations: PASS. Captured in quickstart and plan
  gates.
- Exception handling: PASS WITH DOCUMENTED EXCEPTION. Plain-text varietal field
  is a scoped MVP deviation, documented with follow-up alignment requirement.

## Phase 2: Task Planning Strategy

Tasks will be generated to deliver independently testable slices:

1. Backend foundation: Spring Boot app, config properties for configured owner,
   Mongo repositories, Docker Mongo setup.
2. Import API slice (US1): upload endpoint, CSV parser, validation, persistence,
   import summary, event creation.
3. Bottle list API slice (US2): list endpoint with normalized DTOs.
4. Frontend integration: upload form, summary messaging, list rendering,
   empty/error/loading states aligned to design system.
5. Test hardening: backend unit/integration, frontend behavior tests, and
   minimal end-to-end smoke flow.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Plain-text `WINE.varietal` deviation from canonical grape graph | MVP import speed and reduced initial surface area | Full grape normalization (`WINE_GRAPE` + `GRAPE` alignment) would materially increase implementation scope for this feature |
