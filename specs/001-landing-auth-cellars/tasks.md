# Tasks: Landing, Registration, Login, and Cellars

**Input**: Design documents from `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/`
**Prerequisites**: `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/plan.md`, `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/spec.md`, `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/research.md`, `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/data-model.md`, `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/contracts/openapi.yaml`

**Tests**: Unit tests are mandatory for all business functionality in every phase. Integration, contract, and UI tests are also required for the behaviors named in the specification.

**Organization**: Tasks are grouped by user story so each story can be implemented and verified independently.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Initialize the backend, frontend, and root tooling structure planned for the feature.

- [X] T001 Create the backend and frontend workspace skeleton in `/workspaces/winecellar-spec-kit/backend/` and `/workspaces/winecellar-spec-kit/frontend/`
- [X] T002 Initialize the Spring Boot 4 Maven project and base dependencies in `/workspaces/winecellar-spec-kit/backend/pom.xml`
- [X] T003 [P] Initialize the React + Vite + Tailwind 4 TypeScript app configuration in `/workspaces/winecellar-spec-kit/frontend/package.json`
- [X] T004 [P] Create the frontend toolchain files in `/workspaces/winecellar-spec-kit/frontend/vite.config.ts`, `/workspaces/winecellar-spec-kit/frontend/tailwind.config.ts`, and `/workspaces/winecellar-spec-kit/frontend/tsconfig.json`
- [X] T005 [P] Create the backend application entrypoint and package layout in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/WinecellarApplication.java`
- [X] T006 [P] Create the frontend application entrypoint and route shell in `/workspaces/winecellar-spec-kit/frontend/src/main.tsx` and `/workspaces/winecellar-spec-kit/frontend/src/app/App.tsx`
- [X] T007 Create root-local runtime orchestration in `/workspaces/winecellar-spec-kit/docker-compose.yml`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Build shared infrastructure that all user stories depend on.

**⚠️ CRITICAL**: No user story work should begin until this phase is complete.

- [X] T008 Create backend configuration for MongoDB, session cookie settings, and activation-link logging in `/workspaces/winecellar-spec-kit/backend/src/main/resources/application.yml`
- [X] T009 [P] Implement shared backend error handling and API response models in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/shared/api/ApiExceptionHandler.java` and `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/shared/api/ErrorResponse.java`
- [X] T010 [P] Implement Spring Security baseline and auth filter chain in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/config/SecurityConfig.java`
- [X] T011 [P] Implement backend persistence models and repositories for user accounts, activation tokens, sessions, memberships, cellars, and identity events in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/` and `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/cellar/`
- [X] T012 [P] Implement the activation-delivery port and log-based adapter in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/delivery/ActivationDeliveryPort.java` and `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/delivery/LoggingActivationDeliveryAdapter.java`
- [X] T013 [P] Implement identity-event persistence and append service in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/audit/IdentityAccessEventService.java`
- [X] T014 [P] Translate `/workspaces/winecellar-spec-kit/design/design-system.json` into frontend tokens and global styles in `/workspaces/winecellar-spec-kit/frontend/src/styles/tokens.css` and `/workspaces/winecellar-spec-kit/frontend/src/styles/global.css`
- [X] T015 [P] Implement shared frontend design-system components modeled on `frontend-template` patterns in `/workspaces/winecellar-spec-kit/frontend/src/components/ui/`
- [X] T016 [P] Implement shared frontend API client, auth-state store, and route guard utilities in `/workspaces/winecellar-spec-kit/frontend/src/lib/api/client.ts`, `/workspaces/winecellar-spec-kit/frontend/src/lib/auth/auth-store.ts`, and `/workspaces/winecellar-spec-kit/frontend/src/routes/ProtectedRoute.tsx`
- [X] T017 Create root verification scripts and command wiring for backend, frontend, and compose validation in `/workspaces/winecellar-spec-kit/package.json` or `/workspaces/winecellar-spec-kit/README.md`

**Checkpoint**: Foundation ready. User story phases can begin.

---

## Phase 3: User Story 1 - Discover the Product and Entry Paths (Priority: P1) 🎯 MVP

**Goal**: Deliver the public start page with clear product messaging and working navigation to registration and sign-in.

**Independent Test**: Open `/` while signed out and verify the page explains the product and exposes working links to `/register` and `/sign-in`.

### Tests for User Story 1

- [X] T018 [P] [US1] Add frontend unit tests for landing-page CTA state and route links in `/workspaces/winecellar-spec-kit/frontend/src/features/landing/__tests__/LandingPage.test.tsx`
- [X] T019 [P] [US1] Add frontend route integration test for signed-out public navigation in `/workspaces/winecellar-spec-kit/frontend/src/routes/__tests__/public-routes.test.tsx`

### Implementation for User Story 1

- [X] T020 [P] [US1] Implement the public app header and navigation shell in `/workspaces/winecellar-spec-kit/frontend/src/components/layout/PublicHeader.tsx`
- [X] T021 [P] [US1] Implement the landing hero, feature bands, and CTA sections in `/workspaces/winecellar-spec-kit/frontend/src/features/landing/LandingPage.tsx`
- [X] T022 [US1] Wire the public routes for `/`, `/register`, and `/sign-in` in `/workspaces/winecellar-spec-kit/frontend/src/app/App.tsx`
- [X] T023 [US1] Add landing-page content, terminology, and performance-safe asset usage aligned to the design system in `/workspaces/winecellar-spec-kit/frontend/src/features/landing/content.ts`

**Checkpoint**: User Story 1 is complete and independently testable.

---

## Phase 4: User Story 2 - Register and Activate an Account (Priority: P2)

**Goal**: Let a visitor register, receive a logged activation link valid for 15 minutes, and activate the account once.

**Independent Test**: Submit valid registration data, verify a pending account and activation token are created, copy the logged activation link from backend logs, and activate the account successfully within 15 minutes while expired and reused links are rejected.

### Tests for User Story 2

- [X] T024 [P] [US2] Add backend unit tests for registration business rules, including duplicate-email rejection, in `/workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/auth/application/RegistrationServiceTest.java`
- [X] T025 [P] [US2] Add backend unit tests for activation-token issuance and validation rules in `/workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/auth/application/ActivationServiceTest.java`
- [X] T026 [P] [US2] Add backend unit tests for activation-link logging delivery in `/workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/auth/delivery/LoggingActivationDeliveryAdapterTest.java`
- [X] T027 [P] [US2] Add backend API integration tests for `/api/auth/register` and `/api/auth/activate` in `/workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/auth/api/AuthRegistrationIntegrationTest.java`
- [X] T028 [P] [US2] Add frontend unit tests for registration and activation page state handling in `/workspaces/winecellar-spec-kit/frontend/src/features/auth/__tests__/registration-flow.test.tsx`

### Implementation for User Story 2

- [X] T029 [P] [US2] Implement auth domain models, DTOs, and mappers for `USER_ACCOUNT` and `ACCOUNT_ACTIVATION` in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/domain/`
- [X] T030 [P] [US2] Implement registration and activation application services in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/application/RegistrationService.java` and `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/application/ActivationService.java`
- [X] T031 [US2] Implement registration and activation API endpoints from the contract in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/api/AuthController.java`
- [X] T032 [US2] Implement identity-event emission for account registration and activation events in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/audit/IdentityAccessEventService.java`
- [X] T033 [P] [US2] Implement the registration page, form model, and success messaging in `/workspaces/winecellar-spec-kit/frontend/src/features/auth/RegisterPage.tsx`
- [X] T034 [P] [US2] Implement the activation result page for success, expired, and already-used outcomes in `/workspaces/winecellar-spec-kit/frontend/src/features/auth/ActivatePage.tsx`
- [X] T035 [US2] Wire frontend registration and activation API calls in `/workspaces/winecellar-spec-kit/frontend/src/features/auth/auth-api.ts`
- [X] T036 [US2] Update frontend routing so `/register` and `/activate` work from the public shell in `/workspaces/winecellar-spec-kit/frontend/src/app/App.tsx`

**Checkpoint**: User Story 2 is complete and independently testable.

---

## Phase 5: User Story 3 - Sign In and Reach the Cellars Area (Priority: P3)

**Goal**: Allow an activated user to sign in, maintain a 12-hour session, and access the protected cellars page with an empty state or membership-backed list.

**Independent Test**: Sign in with an activated account, verify `/cellars` loads only when authenticated, confirm no-membership empty state renders correctly, and confirm expired sessions are denied.

### Tests for User Story 3

- [X] T037 [P] [US3] Add backend unit tests for sign-in, invalid-credential rejection, session expiry, and sign-out rules in `/workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/auth/application/SessionServiceTest.java`
- [X] T038 [P] [US3] Add backend unit tests for cellar membership authorization and cellar-summary reads in `/workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/cellar/application/CellarQueryServiceTest.java`
- [X] T039 [P] [US3] Add backend integration tests for `/api/auth/sign-in`, `/api/auth/sign-out`, `/api/auth/session`, and `/api/cellars` in `/workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/auth/api/AuthSessionIntegrationTest.java`
- [X] T040 [P] [US3] Add frontend unit tests for auth-store transitions, protected routes, and cellars empty-state view models in `/workspaces/winecellar-spec-kit/frontend/src/features/cellars/__tests__/cellars-auth-flow.test.tsx`
- [X] T041 [P] [US3] Add frontend route integration tests for redirect-to-sign-in and authenticated cellar access in `/workspaces/winecellar-spec-kit/frontend/src/routes/__tests__/protected-routes.test.tsx`

### Implementation for User Story 3

- [X] T042 [P] [US3] Implement authenticated session domain models and session application services in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/application/SessionService.java`
- [X] T043 [P] [US3] Implement cellar summary query service and membership-backed read model in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/cellar/application/CellarQueryService.java`
- [X] T044 [US3] Extend the auth API with sign-in, sign-out, and current-session endpoints in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/api/AuthController.java`
- [X] T045 [US3] Implement the `/api/cellars` endpoint from the contract in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/cellar/api/CellarController.java`
- [X] T046 [US3] Emit identity events for sign-in success/failure and session-ended outcomes in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/audit/IdentityAccessEventService.java`
- [X] T047 [P] [US3] Implement the sign-in page and sign-in form UX in `/workspaces/winecellar-spec-kit/frontend/src/features/auth/SignInPage.tsx`
- [X] T048 [P] [US3] Implement the authenticated cellars page, empty state, and cellar card list in `/workspaces/winecellar-spec-kit/frontend/src/features/cellars/CellarsPage.tsx`
- [X] T049 [US3] Implement frontend session bootstrap, sign-in, sign-out, and cellar-loading hooks in `/workspaces/winecellar-spec-kit/frontend/src/lib/auth/auth-store.ts` and `/workspaces/winecellar-spec-kit/frontend/src/features/cellars/useCellarsQuery.ts`
- [X] T050 [US3] Wire protected routing and redirect behavior for `/cellars` in `/workspaces/winecellar-spec-kit/frontend/src/app/App.tsx`

**Checkpoint**: User Story 3 is complete and independently testable.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final hardening, contract alignment, performance verification, and documentation.

- [X] T051 [P] Validate the OpenAPI contract against the implemented controllers in `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/contracts/openapi.yaml`
- [ ] T052 [P] Add backend and frontend performance checks for landing render, auth response time, activation-link generation logging, and cellar-list loading against the thresholds in `/workspaces/winecellar-spec-kit/specs/001-landing-auth-cellars/plan.md`
- [ ] T053 Verify Docker Compose local runtime, activation-link logging, and end-to-end smoke flow in `/workspaces/winecellar-spec-kit/docker-compose.yml`
- [X] T054 [P] Update implementation documentation and local run instructions in `/workspaces/winecellar-spec-kit/README.md`
- [ ] T055 Perform cross-cutting cleanup for UX consistency, error copy, and security headers in `/workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/` and `/workspaces/winecellar-spec-kit/frontend/src/`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1: Setup** has no dependencies and starts immediately.
- **Phase 2: Foundational** depends on Phase 1 and blocks all user story work.
- **Phase 3: US1** depends on Phase 2.
- **Phase 4: US2** depends on Phase 2 and can proceed after US1, though the recommended order is P1 then P2.
- **Phase 5: US3** depends on Phase 2 and on the auth artifacts delivered by US2.
- **Phase 6: Polish** depends on the stories you intend to ship being complete.

### User Story Dependencies

- **US1 (P1)**: independent after foundational setup.
- **US2 (P2)**: independent from US1 for backend behavior, but shares frontend shell components created earlier.
- **US3 (P3)**: depends on US2 because sign-in requires activated accounts and the session/auth APIs.

### Within Each User Story

- Unit and integration tests should be written before or alongside implementation and must fail before the corresponding behavior is implemented.
- Backend domain/application services should precede controller wiring.
- Frontend state and API hooks should precede final page route wiring.

### Parallel Opportunities

- `T003` to `T006` can run in parallel during setup.
- `T009` to `T016` can run in parallel within the foundational phase once directories exist.
- In US2, `T024` to `T028` can run in parallel, followed by `T029`, `T030`, `T033`, and `T034`.
- In US3, `T037` to `T041` can run in parallel, followed by `T042`, `T043`, `T047`, and `T048`.
- Polish tasks `T051`, `T052`, and `T054` can run in parallel after core implementation stabilizes.

---

## Parallel Example: User Story 2

```bash
Task: "Add backend unit tests for registration business rules in /workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/auth/application/RegistrationServiceTest.java"
Task: "Add backend unit tests for activation-token issuance and validation rules in /workspaces/winecellar-spec-kit/backend/src/test/java/com/winecellar/auth/application/ActivationServiceTest.java"
Task: "Add frontend unit tests for registration and activation page state handling in /workspaces/winecellar-spec-kit/frontend/src/features/auth/__tests__/registration-flow.test.tsx"
```

## Parallel Example: User Story 3

```bash
Task: "Implement authenticated session domain models and session application services in /workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/auth/application/SessionService.java"
Task: "Implement cellar summary query service and membership-backed read model in /workspaces/winecellar-spec-kit/backend/src/main/java/com/winecellar/cellar/application/CellarQueryService.java"
Task: "Implement the authenticated cellars page, empty state, and cellar card list in /workspaces/winecellar-spec-kit/frontend/src/features/cellars/CellarsPage.tsx"
```

---

## Implementation Strategy

### MVP First

1. Complete Phase 1 and Phase 2.
2. Deliver Phase 3 to establish the public landing and entry paths.
3. Validate US1 independently before moving on.

### Incremental Delivery

1. Add US2 to deliver account creation and activation.
2. Add US3 to deliver protected sign-in and cellar access.
3. Finish with Phase 6 hardening and Compose verification.

### Suggested MVP Scope

- Recommended MVP for the first demo: **US1 only**
- Recommended first end-to-end authenticated milestone: **US1 + US2 + US3**

## Notes

- No separate frontend refactor phase was added because the planned implementation creates a new `/frontend` app rather than refactoring an existing production frontend; `frontend-template` is used as a structural reference only.
- The constitutional exception for `USER_ACCOUNT` audit-event primary targets must remain visible during implementation and review.
