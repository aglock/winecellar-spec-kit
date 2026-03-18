# Implementation Plan: Landing, Registration, Login, and Cellars

**Branch**: `001-landing-auth-cellars` | **Date**: 2026-03-17 | **Spec**: `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/spec.md`
**Input**: Feature specification from `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/spec.md`

## Summary

Deliver a Docker Compose based full-stack foundation for public landing,
registration, activation, sign-in, and authenticated cellar listing. The
backend will use Spring Boot 4 with MongoDB 8 for identity, activation,
session, cellar membership lookup, and audit events; the frontend will be a new
React + Vite + Tailwind 4 application in `/frontend` that mirrors the routing
and presentation patterns of `/frontend-template` while translating them into
the visual language defined by `/workspaces/winecellar-spec-kit/design/design-system.json`.
Email delivery will be designed behind an adapter so Brevo can be adopted
cleanly later, while early iterations only log activation links in the backend
application log.

## Technical Context

**Language/Version**: Java 21 for the backend; TypeScript 5.x for the frontend  
**Backend Language/Version**: Java 21 with Spring Boot 4  
**Frontend Language/Version**: TypeScript 5.x with React 19, Vite 7, Tailwind CSS 4  
**Primary Dependencies**: Spring Boot Web, Spring Security, Spring Data MongoDB, Bean Validation, MongoDB 8, React Router, Tailwind CSS 4  
**Storage**: MongoDB 8 document database with dedicated collections for user accounts, activation tokens, sessions, memberships, cellars, and identity access events  
**Testing**: Unit tests are mandatory for all business functionality in every implementation phase; use JUnit 5 for backend domain/application logic, Spring Boot integration tests, Mongo-backed repository tests, contract validation for OpenAPI, Vitest + React Testing Library for frontend logic/components, ESLint, and `docker compose config` validation  
**Target Platform**: Web application delivered as separate backend and frontend services behind Docker Compose  
**Project Type**: Full-stack application with isolated `backend/` and `frontend/` workspaces  
**UI Design Source**: `/workspaces/winecellar-spec-kit/design/design-system.json` is the required and only valid design reference for all user-facing work  
**Data Modeling Source**: `/workspaces/winecellar-spec-kit/docs/architecture/information-model.mmd` is the required source information model and MUST be translated into a MongoDB data model using document-database principles  
**Performance Goals**: Public pages render first meaningful content in under 2.0s p75 on broadband; auth API endpoints respond in under 300ms p95 excluding delivery I/O; cellar list API responds in under 400ms p95 for users with up to 50 memberships; activation link generation and delivery logging complete within 60s p95 of successful registration  
**Constraints**: Keep identity/access separate from cellar domain records; reuse sitemap terminology and routes; frontend must mimic `/frontend-template` route structure and component composition patterns without copying its obsolete Tailwind 3 setup; Docker Compose must run the full local stack; activation delivery must use a replaceable port so Brevo can be added later while iteration 1 logs activation links locally  
**Scale/Scope**: Initial multi-user foundation for public acquisition and authenticated cellar access, sized for low-to-medium volume beta usage and later extension to selected-cellar workflows

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Information model defined before storage/schema decisions; semantic entities
  and cardinalities are explicit in `data-model.md`.
- Cellar collaboration remains modeled through `CELLAR_MEMBERSHIP`; this
  feature only reads membership to determine cellar visibility.
- Domain boundaries remain separate: identity/access (`USER_ACCOUNT`,
  `ACCOUNT_ACTIVATION`, `AUTHENTICATED_SESSION`), cellar access
  (`CELLAR_MEMBERSHIP`, `CELLAR`), and audit history (`IDENTITY_ACCESS_EVENT`).
- Canonical geography is unchanged by this feature; no new geography fields are
  introduced.
- `docs/resources/grapes-masterlist.json` is unaffected because the feature does
  not modify wine or grape concepts.
- State-changing actions define event history for registration, activation,
  sign-in, sign-out, and session expiry.
- Nullable relationships are explicitly justified in `data-model.md`.
- Code structure is planned around clear backend modules and frontend feature
  folders, favoring readability over indirection.
- Automated test strategy covers registration, activation expiry/reuse, sign-in,
  sign-out, session expiry, cellar-list access control, and frontend route
  protection.
- UX changes preserve sitemap terminology: `Start Page`, `Register`, `Sign In`,
  and `Cellars`.
- Performance expectations are measurable and included above.
- User-facing work references only `/workspaces/winecellar-spec-kit/design/design-system.json`.
- Data design references `/workspaces/winecellar-spec-kit/docs/architecture/information-model.mmd`
  and explains MongoDB aggregate boundaries in `data-model.md`.
- Each phase below defines the tests required before moving on.
- Temporary exception requiring explicit approval before implementation:
  identity audit events need a `USER_ACCOUNT` primary target before any cellar
  exists. That conflicts with the current constitution's event target
  restriction and is documented in Complexity Tracking.

## Project Structure

### Documentation (this feature)

```text
specs/001-landing-auth-cellars/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/winecellar/
│   │   │   ├── auth/
│   │   │   ├── cellar/
│   │   │   ├── shared/
│   │   │   └── config/
│   │   └── resources/
│   └── test/
│       ├── java/com/winecellar/
│       └── resources/
frontend/
├── package.json
├── vite.config.ts
├── tailwind.config.ts
├── src/
│   ├── app/
│   ├── routes/
│   ├── components/
│   ├── features/
│   │   ├── landing/
│   │   ├── auth/
│   │   └── cellars/
│   ├── lib/
│   └── styles/
│       └── tokens.css
docker-compose.yml
```

**Structure Decision**: Use separate `backend/` and `frontend/` applications so
the Spring Boot API, MongoDB integration, and React web client can evolve
independently while still being orchestrated together via root
`docker-compose.yml`. The new `frontend/` intentionally mirrors
`/frontend-template` concepts such as route-driven pages, shared components, and
global design tokens, but upgrades them to TypeScript and Tailwind 4 and keeps
the template as reference-only source material.

**Phase Test Gate**: A phase is complete only after the tests listed under that
phase pass locally.

## Phase 0: Research And Technical Decisions

Produce `research.md` and resolve the open implementation decisions for auth,
session handling, MongoDB aggregates, frontend architecture, email delivery,
and Docker Compose topology.

**Phase 0 test gate**

- Manual review that `research.md` resolves all plan ambiguities.
- Manual review that business functionality identified for later phases has a
  corresponding unit-test strategy.
- `docker compose config` for the planned service topology once the compose file
  exists in implementation.

## Phase 1: Design And Contracts

Produce `data-model.md`, `/contracts/openapi.yaml`, and `quickstart.md`.
Document the MongoDB collections, API contracts, route behavior, role effects,
and local startup flow for backend, frontend, MongoDB, and activation-link logging.

**Phase 1 test gate**

- Contract review against the feature spec and sitemap routes.
- Schema lint or OpenAPI validation for `contracts/openapi.yaml`.
- Manual review that every nullable relationship and event type is documented.
- Manual review that backend and frontend business rules are assigned explicit
  unit-test coverage in the design.

## Phase 2: Implementation Plan

### Backend

1. Create a Spring Boot 4 service in `/backend` with modules for auth, cellar
   listing, shared config, and audit/event persistence.
2. Implement registration, activation, sign-in, sign-out, session validation,
   and current-session lookup endpoints.
3. Persist activation tokens with 15-minute expiry and single-use semantics.
4. Persist 12-hour authenticated sessions in MongoDB and enforce them via a
   secure HTTP-only session cookie or bearer token abstraction selected in
   `research.md`.
5. Implement cellar listing by joining `CELLAR_MEMBERSHIP` to `CELLAR` and
   returning only accessible cellars.
6. Emit identity access events for registration, activation issuance, activation
   success/failure, sign-in success/failure, sign-out, and expiry handling.
7. Implement an activation delivery port with a log-based adapter for early
   iterations and a Brevo adapter planned behind the same interface.

**Backend test gate**

- `mvn test`
- Unit tests for registration, activation-token issuance/validation, session
  expiry rules, cellar-authorization rules, and activation-link logging service
- Targeted integration tests for registration, activation expiry, duplicate
  activation, blocked sign-in before activation, valid sign-in, invalid
  credentials, sign-out, expired session rejection, and authorized cellar list

### Frontend

1. Create a React + Vite + Tailwind 4 application in `/frontend`.
2. Rebuild the route shell from `/frontend-template` in TypeScript with
   `/`, `/register`, `/activate`, `/sign-in`, and `/cellars`.
3. Translate `/workspaces/winecellar-spec-kit/design/design-system.json` into
   Tailwind 4 theme tokens, typography utilities, motion choices, and reusable
   components.
4. Implement landing, registration, activation-result, sign-in, and cellars
   pages with protected routing and consistent feedback surfaces.
5. Use a shared API client for auth/session state and cellar loading.

**Frontend test gate**

- `npm test`
- `npm run lint`
- Unit tests for route guards, auth-state transitions, form-state validation,
  and cellar-list view-model behavior
- Route/component tests for unauthenticated redirect, registration submission,
  activation status messaging, sign-in error handling, and empty/non-empty
  cellars states

### Deployment

1. Add root `docker-compose.yml` for `mongodb`, `backend`, `frontend`, and a
   shared network and volumes required by the composed stack.
2. Configure backend and frontend containers for environment-based service URLs,
   startup ordering, and persistent MongoDB storage.
3. Keep activation-link logging enabled by default in Compose-based local
   development, with Brevo-related configuration reserved for later iterations.
4. Ensure the quickstart uses Compose as the default local runtime path.

**Deployment test gate**

- `docker compose config`
- `docker compose up --build` smoke test
- Verification that backend logs expose activation links in local development
- Manual verification of landing page, registration flow, activation flow,
  sign-in, and `/cellars` access through the composed stack

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Identity access events need a non-cellar target category (`USER_ACCOUNT`) | Registration, activation, and sign-in occur before a user necessarily has cellar context, but the feature requires auditable identity history | Dropping pre-cellar audit events would violate FR-019 through FR-021 and make auth history incomplete |
