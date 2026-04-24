# Implementation Plan: OpenTelemetry Application Monitoring

**Branch**: `002-opentelemetry-monitoring` | **Date**: 2026-04-24 | **Spec**: `specs/002-opentelemetry-monitoring/spec.md`
**Input**: Feature specification from `/specs/002-opentelemetry-monitoring/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Add backend observability with OpenTelemetry traces and metrics for critical
operations (CSV import and bottle list), routed through OpenTelemetry Collector
to Jaeger and Prometheus. Enforce sampling and export resiliency policies from
clarifications, deliver rollout across dev/staging/prod, and keep access to
monitoring UIs limited to authenticated operations/admin users.

## Technical Context

<!--
  ACTION REQUIRED: Start from the project defaults below and refine them only
  when the feature requires additional detail.
-->

**Backend Language/Version**: Java 21 + Spring Boot 4.0.6  
**Frontend Language/Version**: Vue 3.5 + Vue Router 4.5 + Vite 8 + Tailwind 3.4 (no frontend telemetry in this feature)  
**Primary Dependencies**: spring-boot-starter-actuator, micrometer tracing bridge (OpenTelemetry), OTLP exporter, Prometheus registry, OpenTelemetry Collector, Jaeger, Prometheus  
**Storage**: MongoDB 8 for business data; Jaeger and Prometheus for observability telemetry storage/query  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc/WebTestClient for backend behavior, plus observability smoke checks (Jaeger trace visibility, Prometheus scrape, outage resiliency)  
**Target Platform**: Web application backend APIs with operational observability stack  
**Project Type**: Full-stack application (backend-focused feature slice)  
**UI Design Source**: `design/design-system.json` is the required and only valid design reference for all user-facing work  
**Data Modeling Source**: `docs/architecture/information-model.mmd` is the required source information model and MUST be translated into a MongoDB data model using document-database principles  
**Performance Goals**: 95%+ visibility for critical operations within 60s; telemetry overhead <= 10% median request-time increase; sampled successful traces remain within 10% +/- 2%; no telemetry-induced availability regression during 10-minute exporter outage simulation  
**Constraints**: Traces + metrics only (logs out of scope); 10% head sampling for successful requests with always-sampled failures; non-blocking bounded-queue export policy with retry and drop-oldest overflow; rollout in dev/staging/prod; dashboard/UI access limited to authenticated operations/admin users; any frontend implementation changes for this and future features MUST be made only under `frontend-vue/`  
**Scale/Scope**: Backend API observability only for this increment; critical operations are CSV import and bottle list retrieval

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Information model defined before implementation details: PASS. Observability
  entities and relationships are explicit in design artifacts.
- Cellar collaboration and role model: PASS. Feature does not change membership
  model; telemetry preserves role-aware context for cellar operations.
- Domain boundaries remain separate: PASS. Observability model is additive and
  does not collapse business domain entities.
- Canonical geography consistency: PASS (no changes to geography concepts).
- Grape master-data alignment: PASS (no changes).
- Evented audit trail: PASS. Existing business event model remains intact;
  telemetry adds operational traces/metrics without replacing domain events.
- Nullable relationships justification: PASS. Only observability root-span
  parent references are nullable by design.
- Code quality/maintainability expectations: PASS. Implementation will keep
  instrumentation concerns isolated (config/infrastructure) and controllers
  thin.
- GoF preference and role naming: PASS. If wrappers/builders are introduced,
  pattern-role naming will be explicit.
- Automated test strategy: PASS. Includes behavior tests plus observability
  smoke checks.
- UX consistency: PASS. Operational naming mirrors existing import/cellar terms.
- Performance expectations measurable: PASS. Targets defined above.
- User-facing design system reference: PASS (no direct UI redesign in scope).
- Data-design reference to information model: PASS. Business data model is
  unchanged; observability model is documented separately.
- Phase test gate enforcement: PASS. Explicit gates defined below.

## Project Structure

### Documentation (this feature)

```text
specs/002-opentelemetry-monitoring/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── observability-contract.yaml
└── tasks.md
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
backend/
├── pom.xml
├── src/main/java/com/winecellar/importer/
│   ├── api/
│   ├── application/
│   ├── domain/
│   ├── infrastructure/
│   └── config/
└── src/main/resources/

docker/
├── mongo-compose.yml
├── observability-compose.yml
├── otel-collector-config.yml
└── prometheus.yml

frontend-vue/
└── src/
```

**Structure Decision**: Implement observability primarily in `backend/` and
`docker/` with minimal/no feature-code changes in `frontend-vue/`. Keep
instrumentation configuration in backend config/infrastructure layers and keep
the observability runtime stack as dedicated Docker Compose artifacts.

**Phase Test Gate**:

- End of Phase 0 (research): verify all technical uncertainties are resolved in
  `research.md` and none remain marked unresolved.
- End of Phase 1 (design/contracts): run:
  - `docker compose -f docker/observability-compose.yml config`
  - artifact consistency check across `spec.md`, `plan.md`, `data-model.md`,
    and `contracts/observability-contract.yaml`
- End of implementation phase (outside this command): run:
  - `cd backend && mvn test`
  - observability smoke flow from `quickstart.md`

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |

## Phase 0: Research And Decisions

Research output is documented in `specs/002-opentelemetry-monitoring/research.md`.

Resolved topics:

- Spring Boot observability integration approach
- Collector routing design for traces/metrics
- Sampling policy and error visibility guarantees
- Export resiliency behavior during backend outages
- Rollout scope and dashboard access controls
- Operational interface contract boundaries

All technical unknowns are resolved with explicit rationale and alternatives.

## Phase 1: Design And Contracts

Design outputs are documented in:

- `specs/002-opentelemetry-monitoring/data-model.md`
- `specs/002-opentelemetry-monitoring/contracts/observability-contract.yaml`
- `specs/002-opentelemetry-monitoring/quickstart.md`

Design scope includes:

- Observability information model for operations/traces/metrics/policies
- Contracted operational interfaces and OTLP ingest assumptions
- Runbook-style local setup and verification for Jaeger + Prometheus + Collector

## Constitution Check (Post-Design)

- Information-model-first principle: PASS. Observability entities are explicit
  and separate from storage/runtime tools.
- Collaboration and access-control principle: PASS. Cellar permissions are not
  changed; telemetry records role-aware context where relevant.
- Domain-boundary separation: PASS. No merge of master-data/inventory/identity.
- Evented audit-trail principle: PASS. Domain events remain authoritative;
  observability is additive for operational diagnostics.
- Canonical concepts and geography: PASS (no impact).
- Code quality and maintainability: PASS by planned separation of
  instrumentation configuration from domain logic.
- Testing standards and performance obligations: PASS with explicit measurable
  targets and phase test gates.

## Phase 2: Task Planning Strategy

Tasks will be generated as independently testable slices:

1. Backend instrumentation foundation (Actuator, OTel bridge, OTLP exporters,
   baseline attributes).
2. Collector and backend export policy alignment (sampling + queue/retry policy).
3. Operational endpoints and security controls for dashboard/UI access.
4. Environment rollout wiring (dev, staging, production) and config profiles.
5. Verification automation and smoke checks for traces, metrics, and outage
   resiliency.
