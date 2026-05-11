# Tasks: OpenTelemetry Application Monitoring

**Input**: Design documents from `/specs/002-opentelemetry-monitoring/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/observability-contract.yaml, quickstart.md

**Tests**: Test tasks are required for this feature because behavior changes observability coverage, resiliency, and security.

**Organization**: Tasks are grouped by user story for independent implementation and verification.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Prepare dependencies and baseline project wiring for observability work.

- [X] T001 Update backend dependencies for observability in backend/pom.xml
- [X] T002 Add observability base application properties in backend/src/main/resources/application.yml
- [X] T003 [P] Add dev profile observability settings in backend/src/main/resources/application-dev.yml
- [X] T004 [P] Add feature notes and local run command updates in backend/README.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Implement core observability and resiliency building blocks required by all stories.

**CRITICAL**: No user story work starts before this phase completes.

- [ ] T005 Create typed observability configuration properties in backend/src/main/java/com/winecellar/importer/config/observability/ObservabilityProperties.java
- [ ] T006 Implement OpenTelemetry resource and exporter configuration in backend/src/main/java/com/winecellar/importer/config/observability/OpenTelemetryConfiguration.java
- [ ] T007 Implement sampling policy configuration (10% success, always sample errors) in backend/src/main/java/com/winecellar/importer/config/observability/SamplingConfiguration.java
- [ ] T008 Implement bounded-queue non-blocking export policy wiring in backend/src/main/java/com/winecellar/importer/config/observability/ExportResilienceConfiguration.java
- [ ] T009 [P] Add observability docker stack validation task notes in docker/observability-compose.yml
- [ ] T010 [P] Add collector resilience defaults in docker/otel-collector-config.yml

**Checkpoint**: Foundation complete and ready for story-level implementation.

---

## Phase 3: User Story 1 - End-to-End Operational Visibility (Priority: P1) MVP

**Goal**: Make critical operations traceable end-to-end with correlated traces and core request telemetry.

**Independent Test**: Trigger CSV import and bottle listing, then verify correlated traces with status and duration in Jaeger.

### Tests for User Story 1

- [ ] T011 [P] [US1] Add contract test for health endpoint in backend/src/test/java/com/winecellar/importer/api/ObservabilityHealthContractTest.java
- [ ] T012 [P] [US1] Add contract test for prometheus endpoint in backend/src/test/java/com/winecellar/importer/api/ObservabilityPrometheusContractTest.java
- [ ] T013 [P] [US1] Add integration test for trace emission on CSV import in backend/src/test/java/com/winecellar/importer/integration/ImportTracingIntegrationTest.java
- [ ] T014 [P] [US1] Add integration test for trace emission on bottle list retrieval in backend/src/test/java/com/winecellar/importer/integration/BottleListTracingIntegrationTest.java

### Implementation for User Story 1

- [ ] T015 [US1] Implement request-span naming and correlation conventions in backend/src/main/java/com/winecellar/importer/infrastructure/observability/RequestObservationConvention.java
- [ ] T016 [US1] Add import operation span attributes in backend/src/main/java/com/winecellar/importer/application/imports/WineBottleCsvImportService.java
- [ ] T017 [US1] Add bottle-list operation span attributes in backend/src/main/java/com/winecellar/importer/api/BottleListController.java
- [ ] T018 [US1] Expose and verify actuator endpoints required by contract in backend/src/main/resources/application.yml

**Checkpoint**: US1 is independently functional and observable end-to-end.

---

## Phase 4: User Story 2 - Fast Incident Triage (Priority: P2)

**Goal**: Provide reliable throughput, latency, and error telemetry for incident triage.

**Independent Test**: Generate success/failure traffic and verify metrics and error telemetry update correctly.

### Tests for User Story 2

- [ ] T019 [P] [US2] Add integration test for request latency metric publication in backend/src/test/java/com/winecellar/importer/integration/LatencyMetricsIntegrationTest.java
- [ ] T020 [P] [US2] Add integration test for error-rate metric publication in backend/src/test/java/com/winecellar/importer/integration/ErrorMetricsIntegrationTest.java
- [ ] T021 [P] [US2] Add integration test for resiliency during exporter outage in backend/src/test/java/com/winecellar/importer/integration/ExporterOutageResilienceIntegrationTest.java

### Implementation for User Story 2

- [ ] T022 [US2] Implement metric tags and histogram settings for critical operations in backend/src/main/java/com/winecellar/importer/config/observability/MetricsConfiguration.java
- [ ] T023 [US2] Implement structured error classification attributes in backend/src/main/java/com/winecellar/importer/infrastructure/observability/ErrorTelemetryMapper.java
- [ ] T024 [US2] Configure retry, queue bounds, and drop-oldest behavior in backend/src/main/resources/application.yml

**Checkpoint**: US2 independently supports incident triage metrics and outage-safe telemetry behavior.

---

## Phase 5: User Story 3 - Environment and User-Safe Observability (Priority: P3)

**Goal**: Ensure telemetry is environment-labeled and excludes sensitive data while preserving useful context.

**Independent Test**: Compare dev/staging/prod telemetry labels and verify sensitive fields are not emitted.

### Tests for User Story 3

- [ ] T025 [P] [US3] Add integration test for required environment and service labels in backend/src/test/java/com/winecellar/importer/integration/TelemetryResourceLabelsIntegrationTest.java
- [ ] T026 [P] [US3] Add integration test for sensitive attribute redaction in backend/src/test/java/com/winecellar/importer/integration/SensitiveDataRedactionIntegrationTest.java
- [ ] T027 [P] [US3] Add integration test for access control policy wiring in backend/src/test/java/com/winecellar/importer/integration/ObservabilityAccessPolicyIntegrationTest.java

### Implementation for User Story 3

- [ ] T028 [US3] Implement telemetry attribute filtering and redaction policy in backend/src/main/java/com/winecellar/importer/infrastructure/observability/TelemetrySanitizer.java
- [ ] T029 [US3] Implement environment/service resource labeling for dev, staging, and prod in backend/src/main/java/com/winecellar/importer/config/observability/ResourceAttributesConfiguration.java
- [ ] T030 [US3] Document operations/admin-only dashboard access policy in specs/002-opentelemetry-monitoring/quickstart.md

**Checkpoint**: US3 independently enforces safe telemetry and environment consistency.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final hardening, validation, and documentation updates across stories.

- [ ] T031 [P] Run and record backend test suite results for observability feature in backend/README.md
- [ ] T032 [P] Validate docker observability stack contract and startup flow in docker/observability-compose.yml
- [ ] T033 Perform quickstart end-to-end verification and update notes in specs/002-opentelemetry-monitoring/quickstart.md
- [ ] T034 Run full quality gate and capture command set in specs/002-opentelemetry-monitoring/plan.md

---

## Dependencies & Execution Order

### Phase Dependencies

- Setup (Phase 1): starts immediately.
- Foundational (Phase 2): depends on Setup completion.
- User Stories (Phase 3 to Phase 5): all depend on Foundational completion.
- Polish (Phase 6): depends on all selected stories being complete.

### User Story Dependencies

- US1 (P1): depends only on Foundational phase.
- US2 (P2): depends only on Foundational phase and can run independently of US1.
- US3 (P3): depends only on Foundational phase and can run independently of US1/US2.

### Within Each User Story

- Tests are created first and must fail before implementation.
- Configuration and model wiring before controller/service integration.
- Story-level checkpoint must pass before moving on.

---

## Parallel Execution Examples

### User Story 1

- Run T011, T012, T013, and T014 in parallel.
- Run T016 and T017 in parallel after T015.

### User Story 2

- Run T019, T020, and T021 in parallel.
- Run T022 and T023 in parallel before T024.

### User Story 3

- Run T025, T026, and T027 in parallel.
- Run T028 and T029 in parallel before T030.

---

## Implementation Strategy

### MVP First (US1)

1. Complete Phase 1 and Phase 2.
2. Deliver Phase 3 (US1) and validate trace visibility for import and bottle list.
3. Demo MVP observability value.

### Incremental Delivery

1. Add US2 for triage metrics and outage resiliency.
2. Add US3 for environment labels and safe telemetry constraints.
3. Finish with Phase 6 polish and full verification.

### Team Parallel Strategy

1. Team aligns on Phase 1 and Phase 2.
2. Split US1, US2, and US3 across developers after foundational completion.
3. Merge at Phase 6 with integrated validation.
