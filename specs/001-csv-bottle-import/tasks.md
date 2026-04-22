# Tasks: CSV Bottle Import and Cellar View

**Input**: Design documents from `/specs/001-csv-bottle-import/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/csv-import-api.yaml, quickstart.md

**Tests**: Test tasks are included because this feature changes behavior and introduces new API surfaces.

**Organization**: Tasks are grouped by user story so each story can be implemented and validated independently.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Initialize backend, frontend integration points, and local database runtime.

- [X] T001 Create Spring Boot Maven project skeleton in `backend/pom.xml` and `backend/src/main/java/com/winecellar/importer/ImporterApplication.java`
- [X] T002 [P] Add backend base configuration files in `backend/src/main/resources/application.yml` and `backend/src/main/resources/application-dev.yml`
- [X] T003 [P] Add owner startup configuration model in `backend/src/main/java/com/winecellar/importer/config/OwnerProperties.java`
- [X] T004 [P] Add MongoDB Docker compose runtime in `docker/mongo-compose.yml`
- [X] T005 Configure frontend API base URL handling in `frontend/src/services/apiClient.js`
- [X] T006 [P] Add backend and frontend README run instructions in `backend/README.md` and `frontend/README.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core domain model, persistence, shared API conventions, and bootstrap data required by all stories.

**⚠️ CRITICAL**: No user story implementation starts before this phase is complete.

- [X] T007 Define shared API error response DTOs in `backend/src/main/java/com/winecellar/importer/api/error/ApiErrorResponse.java`
- [X] T008 Define global exception handling in `backend/src/main/java/com/winecellar/importer/api/error/GlobalExceptionHandler.java`
- [X] T009 [P] Implement core Mongo entities `CellarDocument` and `CompartmentDocument` in `backend/src/main/java/com/winecellar/importer/infrastructure/mongo/cellar/`
- [X] T010 [P] Implement canonical reference Mongo entities (`ProducerDocument`, `CountryDocument`, `RegionDocument`, `AppellationDocument`, `BottleSizeDocument`) in `backend/src/main/java/com/winecellar/importer/infrastructure/mongo/reference/`
- [X] T011 [P] Implement wine, bottle, and event Mongo entities in `backend/src/main/java/com/winecellar/importer/infrastructure/mongo/inventory/`
- [X] T012 Create Spring Data repositories for all foundational entities in `backend/src/main/java/com/winecellar/importer/infrastructure/mongo/**/**Repository.java`
- [X] T013 Implement startup bootstrap for default cellar and default compartment in `backend/src/main/java/com/winecellar/importer/config/DefaultCellarBootstrap.java`
- [X] T014 [P] Add owner context provider using configured startup identity in `backend/src/main/java/com/winecellar/importer/application/OwnerContextProvider.java`
- [X] T015 [P] Add shared mapping utilities for normalization/sentinel handling in `backend/src/main/java/com/winecellar/importer/application/imports/CsvValueNormalizer.java`
- [X] T016 Create backend integration test base with Testcontainers MongoDB in `backend/src/test/java/com/winecellar/importer/support/IntegrationTestBase.java`

**Checkpoint**: Foundation ready. User story tasks can now proceed.

---

## Phase 3: User Story 1 - Import Wine Bottles from CSV (Priority: P1) 🎯 MVP

**Goal**: Allow upload of CSV bottle files, persist valid rows, report skipped rows, and emit import event.

**Independent Test**: Upload `docs/resources/cellar-data.csv` and verify import summary plus persisted rows and event.

### Tests for User Story 1

- [X] T017 [P] [US1] Add API contract test for `POST /api/imports/wine-bottles` in `backend/src/test/java/com/winecellar/importer/api/ImportControllerContractTest.java`
- [X] T018 [P] [US1] Add application service unit tests for CSV row validation and sentinel handling in `backend/src/test/java/com/winecellar/importer/application/imports/WineBottleCsvImportServiceTest.java`
- [X] T019 [P] [US1] Add integration test for import persistence and event creation in `backend/src/test/java/com/winecellar/importer/integration/CsvImportIntegrationTest.java`

### Implementation for User Story 1

- [X] T020 [US1] Implement import request/response DTOs in `backend/src/main/java/com/winecellar/importer/api/imports/dto/`
- [X] T021 [US1] Implement CSV parser adapter using Apache Commons CSV in `backend/src/main/java/com/winecellar/importer/infrastructure/csv/CommonsCsvParser.java`
- [X] T022 [US1] Implement canonical entity resolver/upsert logic for producer/geography/bottle size in `backend/src/main/java/com/winecellar/importer/application/imports/CanonicalReferenceResolver.java`
- [X] T023 [US1] Implement wine and bottle persistence orchestration in `backend/src/main/java/com/winecellar/importer/application/imports/WineBottleCsvImportService.java`
- [X] T024 [US1] Implement import event writer with primary target `CELLAR` in `backend/src/main/java/com/winecellar/importer/application/imports/ImportEventRecorder.java`
- [X] T025 [US1] Implement multipart upload REST endpoint in `backend/src/main/java/com/winecellar/importer/api/imports/ImportController.java`
- [X] T026 [US1] Wire CSV import flow into frontend upload service in `frontend/src/services/importService.js`
- [X] T027 [US1] Implement upload UI, summary feedback, and row-skip reporting in `frontend/src/pages/CellarOverview.jsx`

**Checkpoint**: US1 is independently functional and demoable as MVP.

---

## Phase 4: User Story 2 - View Cellar Bottle List (Priority: P2)

**Goal**: Show imported bottles in a structured list with required attributes and proper empty state.

**Independent Test**: Seed bottles directly in MongoDB and verify list endpoint + UI rendering without using import flow.

### Tests for User Story 2

- [X] T028 [P] [US2] Add API contract test for `GET /api/cellars/default/bottles` in `backend/src/test/java/com/winecellar/importer/api/BottleListControllerContractTest.java`
- [X] T029 [P] [US2] Add repository/query integration test for bottle list projection in `backend/src/test/java/com/winecellar/importer/integration/BottleListIntegrationTest.java`
- [X] T030 [P] [US2] Add frontend component test for bottle list, NV display, and empty state in `frontend/tests/CellarBottleList.test.jsx`

### Implementation for User Story 2

- [X] T031 [US2] Implement bottle list query DTO/projection in `backend/src/main/java/com/winecellar/importer/application/bottles/dto/BottleListItemDto.java`
- [X] T032 [US2] Implement bottle listing service with pagination in `backend/src/main/java/com/winecellar/importer/application/bottles/ListDefaultCellarBottlesService.java`
- [X] T033 [US2] Implement bottle list REST endpoint in `backend/src/main/java/com/winecellar/importer/api/bottles/BottleListController.java`
- [X] T034 [US2] Implement frontend bottle list API client in `frontend/src/services/bottleService.js`
- [X] T035 [US2] Implement cellar bottle list view with design-system styling in `frontend/src/pages/CellarView.jsx`
- [X] T036 [US2] Connect post-import refresh flow so newly imported bottles appear immediately in `frontend/src/App.jsx`

**Checkpoint**: US1 and US2 both work independently.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Final hardening, UX consistency, and release readiness.

- [X] T037 [P] Add backend request logging and import timing metrics in `backend/src/main/java/com/winecellar/importer/config/WebLoggingConfig.java`
- [X] T038 [P] Add frontend loading/error/empty-state copy alignment with design system in `frontend/src/components/index.jsx`
- [X] T039 Validate quickstart flow and update commands if needed in `specs/001-csv-bottle-import/quickstart.md`
- [X] T040 Run full backend and frontend test suites and record results in `specs/001-csv-bottle-import/checklists/requirements.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: No dependencies.
- **Phase 2 (Foundational)**: Depends on Phase 1; blocks all user stories.
- **Phase 3 (US1)**: Depends on Phase 2 completion.
- **Phase 4 (US2)**: Depends on Phase 2 completion; can be developed after US1 MVP validation or in parallel once shared contracts are stable.
- **Phase 5 (Polish)**: Depends on desired user stories being complete.

### User Story Dependencies

- **US1 (P1)**: Independent after foundation; defines import capability.
- **US2 (P2)**: Independent after foundation; integrates with US1 for live refresh but remains testable with seeded data.

### Within Each User Story

- Tests first and failing before implementation.
- DTOs and service logic before controllers.
- Backend endpoint before frontend wiring.
- Story must pass its own tests before moving on.

---

## Parallel Opportunities

- Setup tasks `T002`, `T003`, `T004`, and `T006` can run in parallel.
- Foundational entity tasks `T009`, `T010`, and `T011` can run in parallel.
- US1 tests `T017`, `T018`, and `T019` can run in parallel.
- US2 tests `T028`, `T029`, and `T030` can run in parallel.
- Polish tasks `T037` and `T038` can run in parallel.

---

## Parallel Example: User Story 1

```bash
# Parallel test work
T017: backend/src/test/java/com/winecellar/importer/api/ImportControllerContractTest.java
T018: backend/src/test/java/com/winecellar/importer/application/imports/WineBottleCsvImportServiceTest.java
T019: backend/src/test/java/com/winecellar/importer/integration/CsvImportIntegrationTest.java

# Parallel implementation prep
T021: backend/src/main/java/com/winecellar/importer/infrastructure/csv/CommonsCsvParser.java
T022: backend/src/main/java/com/winecellar/importer/application/imports/CanonicalReferenceResolver.java
T024: backend/src/main/java/com/winecellar/importer/application/imports/ImportEventRecorder.java
```

---

## Implementation Strategy

### MVP First (US1 only)

1. Complete Phase 1 and Phase 2.
2. Deliver Phase 3 (US1).
3. Validate import with `docs/resources/cellar-data.csv`.
4. Demo MVP.

### Incremental Delivery

1. Foundation complete.
2. Deliver US1 and validate independently.
3. Deliver US2 and validate independently.
4. Apply polish and full test run.

### Parallel Team Strategy

1. Team aligns on Phase 1 and Phase 2.
2. Backend-focused developer drives import/list APIs.
3. Frontend-focused developer integrates upload/list UI against contracts.
4. Testing is executed in parallel where tasks are marked `[P]`.
