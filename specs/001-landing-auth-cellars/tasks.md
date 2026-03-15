# Tasks: Landing, Registration, Login, and Cellars

**Input**: Design documents from `/specs/001-landing-auth-cellars/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Test tasks are required for this feature because the specification
explicitly requires automated coverage for registration, activation expiry,
sign-in, session duration, authenticated cellar access, and unauthenticated
access denial.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Backend**: `backend/src/main/java/`, `backend/src/test/java/`
- **Frontend**: `frontend/src/`, `frontend/tests/`
- **Feature docs**: `specs/001-landing-auth-cellars/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Establish the backend and frontend project scaffolding required by
the implementation plan.

- [ ] T001 Create the Spring Boot backend build configuration in `backend/pom.xml`
- [ ] T002 Create the frontend package manifest and scripts in `frontend/package.json`
- [ ] T003 [P] Configure Vite for the React Native Web frontend in `frontend/vite.config.ts`
- [ ] T004 [P] Configure Tailwind CSS theme integration in `frontend/tailwind.config.js`
- [ ] T005 [P] Configure frontend TypeScript compilation in `frontend/tsconfig.json`
- [ ] T006 Create the backend application startup smoke test in `backend/src/test/java/com/winecellar/BootstrapSmokeTest.java`
- [ ] T007 Create the frontend route-shell smoke test in `frontend/tests/app/app-router-smoke.test.tsx`
- [ ] T008 Run the setup phase test and validation steps documented in `specs/001-landing-auth-cellars/quickstart.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [ ] T009 Create the Spring Boot application bootstrap in `backend/src/main/java/com/winecellar/WineCellarApplication.java`
- [ ] T010 [P] Create backend security and session baseline configuration in `backend/src/main/java/com/winecellar/config/SecurityConfig.java`
- [ ] T011 [P] Create the user account MongoDB document with persisted password hash in `backend/src/main/java/com/winecellar/access/model/UserAccountDocument.java`
- [ ] T012 [P] Create the account activation MongoDB document in `backend/src/main/java/com/winecellar/access/model/AccountActivationDocument.java`
- [ ] T013 [P] Create the authenticated session MongoDB document in `backend/src/main/java/com/winecellar/access/model/AuthSessionDocument.java`
- [ ] T014 [P] Create the identity access event MongoDB document in `backend/src/main/java/com/winecellar/access/model/AccessEventDocument.java`
- [ ] T015 [P] Create the cellar MongoDB document in `backend/src/main/java/com/winecellar/cellar/model/CellarDocument.java`
- [ ] T016 [P] Create the cellar membership MongoDB document in `backend/src/main/java/com/winecellar/cellar/model/CellarMembershipDocument.java`
- [ ] T017 [P] Create shared backend API exception handling in `backend/src/main/java/com/winecellar/api/ApiExceptionHandler.java`
- [ ] T018 [P] Create the frontend application router shell in `frontend/src/app/AppRouter.tsx`
- [ ] T019 [P] Create the shared frontend auth client in `frontend/src/services/authClient.ts`
- [ ] T020 Run the foundational phase test and validation steps documented in `specs/001-landing-auth-cellars/quickstart.md`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Discover the Product and Entry Paths (Priority: P1) 🎯 MVP

**Goal**: Deliver the public start page so signed-out visitors can understand
the product and navigate to registration or sign-in.

**Independent Test**: Open the public start page while signed out and verify
the product explanation and navigation to registration and login work without
depending on the protected cellar area.

### Tests for User Story 1 ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T021 [P] [US1] Create the landing page route test in `frontend/tests/routes/landing-page.test.tsx`
- [ ] T022 [P] [US1] Create the public navigation end-to-end test in `frontend/tests/e2e/landing-navigation.spec.ts`

### Implementation for User Story 1

- [ ] T023 [US1] Implement the public shell and top navigation in `frontend/src/components/layout/PublicShell.tsx`
- [ ] T024 [US1] Implement the start page in `frontend/src/pages/StartPage.tsx`
- [ ] T025 [US1] Wire the public routes `/`, `/register`, `/activate`, and `/sign-in` in `frontend/src/app/routes/publicRoutes.tsx`
- [ ] T026 [US1] Add start page content and CTA copy definitions in `frontend/src/pages/startPageContent.ts`
- [ ] T027 [US1] Run the User Story 1 validation steps in `specs/001-landing-auth-cellars/quickstart.md`

**Checkpoint**: User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Register and Activate an Account (Priority: P2)

**Goal**: Allow a new user to register, receive a 15-minute activation link,
and activate the account before sign-in.

**Independent Test**: Submit the registration form, verify the activation link
is issued and expires after 15 minutes, and confirm sign-in remains blocked
until activation succeeds.

### Tests for User Story 2 ⚠️

- [ ] T028 [P] [US2] Create the registration and activation contract test in `backend/src/test/java/com/winecellar/contracts/AuthRegistrationContractTest.java`
- [ ] T029 [P] [US2] Create the registration and activation integration test with password-hash persistence checks in `backend/src/test/java/com/winecellar/access/AuthRegistrationIntegrationTest.java`
- [ ] T030 [P] [US2] Create the frontend registration flow test in `frontend/tests/routes/registration-flow.test.tsx`
- [ ] T031 [P] [US2] Create the identity event and activation email test in `backend/src/test/java/com/winecellar/access/RegistrationAuditAndEmailTest.java`

### Implementation for User Story 2

- [ ] T032 [P] [US2] Create the user account repository in `backend/src/main/java/com/winecellar/access/repository/UserAccountRepository.java`
- [ ] T033 [P] [US2] Create the account activation repository in `backend/src/main/java/com/winecellar/access/repository/AccountActivationRepository.java`
- [ ] T034 [P] [US2] Create the identity access event repository in `backend/src/main/java/com/winecellar/access/repository/AccessEventRepository.java`
- [ ] T035 [US2] Implement activation email dispatch in `backend/src/main/java/com/winecellar/access/service/ActivationEmailService.java`
- [ ] T036 [US2] Implement registration with password hashing, activation issuance, and audit events in `backend/src/main/java/com/winecellar/access/service/RegistrationService.java`
- [ ] T037 [US2] Implement activation validation, completion, and audit events in `backend/src/main/java/com/winecellar/access/service/ActivationService.java`
- [ ] T038 [US2] Implement the registration and activation API controller in `backend/src/main/java/com/winecellar/access/api/AuthRegistrationController.java`
- [ ] T039 [US2] Implement the registration page on route `/register` in `frontend/src/pages/RegisterPage.tsx`
- [ ] T040 [US2] Implement the activation result page on route `/activate` in `frontend/src/pages/ActivateAccountPage.tsx`
- [ ] T041 [US2] Connect the registration UI to backend auth calls in `frontend/src/services/registerAccount.ts`
- [ ] T042 [US2] Run the User Story 2 validation steps in `specs/001-landing-auth-cellars/quickstart.md`

**Checkpoint**: User Stories 1 and 2 should both work independently

---

## Phase 5: User Story 3 - Sign In and Reach the Cellars Area (Priority: P3)

**Goal**: Let an activated user sign in, maintain a 12-hour session, and reach
the authenticated cellars page with either a cellar list or an empty state.

**Independent Test**: Sign in with an activated account, confirm the session is
treated as valid for 12 hours, verify the cellars page requires authentication,
and confirm the empty state appears when the user has no memberships.

### Tests for User Story 3 ⚠️

- [ ] T043 [P] [US3] Create the login-session-cellars contract test in `backend/src/test/java/com/winecellar/contracts/AuthSessionCellarsContractTest.java`
- [ ] T044 [P] [US3] Create the login-session-cellars integration test with password-hash verification checks in `backend/src/test/java/com/winecellar/access/AuthSessionCellarsIntegrationTest.java`
- [ ] T045 [P] [US3] Create the frontend login and protected routing test in `frontend/tests/routes/login-cellars-flow.test.tsx`
- [ ] T046 [P] [US3] Create the login and session-end audit event test in `backend/src/test/java/com/winecellar/access/AuthAuditEventIntegrationTest.java`

### Implementation for User Story 3

- [ ] T047 [P] [US3] Create the authenticated session repository in `backend/src/main/java/com/winecellar/access/repository/AuthSessionRepository.java`
- [ ] T048 [P] [US3] Create the cellar repository in `backend/src/main/java/com/winecellar/cellar/repository/CellarRepository.java`
- [ ] T049 [P] [US3] Create the cellar membership repository in `backend/src/main/java/com/winecellar/cellar/repository/CellarMembershipRepository.java`
- [ ] T050 [US3] Implement login with password-hash verification and session lifecycle audit events in `backend/src/main/java/com/winecellar/access/service/LoginService.java`
- [ ] T051 [US3] Implement cellar summary retrieval for authenticated users in `backend/src/main/java/com/winecellar/cellar/service/CellarSummaryService.java`
- [ ] T052 [US3] Implement the login and session inspection API controller in `backend/src/main/java/com/winecellar/access/api/AuthSessionController.java`
- [ ] T053 [US3] Implement the cellars listing API controller in `backend/src/main/java/com/winecellar/cellar/api/CellarsController.java`
- [ ] T054 [US3] Implement the login page on route `/sign-in` in `frontend/src/pages/LoginPage.tsx`
- [ ] T055 [US3] Implement the authenticated session store and protected route gate in `frontend/src/services/sessionStore.ts`
- [ ] T056 [US3] Implement the cellars page on route `/cellars` with list and empty state handling in `frontend/src/pages/CellarsPage.tsx`
- [ ] T057 [US3] Run the User Story 3 validation steps in `specs/001-landing-auth-cellars/quickstart.md`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T058 [P] Add end-to-end coverage for the full auth-to-cellars journey in `frontend/tests/e2e/auth-cellars-journey.spec.ts`
- [ ] T059 [P] Add backend performance verification for registration, activation, login, and cellar listing in `backend/src/test/java/com/winecellar/performance/AuthCellarsPerformanceTest.java`
- [ ] T060 [P] Add frontend performance verification for landing and cellars page rendering in `frontend/tests/performance/page-render.performance.test.tsx`
- [ ] T061 [P] Refine page-level UX consistency and shared interaction states in `frontend/src/components/layout/PublicShell.tsx`
- [ ] T062 Harden activation expiry and session expiry enforcement in `backend/src/main/java/com/winecellar/config/SecurityConfig.java`
- [ ] T063 [P] Update implementation and validation notes in `specs/001-landing-auth-cellars/quickstart.md`
- [ ] T064 Run the full feature validation checklist in `specs/001-landing-auth-cellars/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational completion
- **User Story 2 (Phase 4)**: Depends on Foundational completion
- **User Story 3 (Phase 5)**: Depends on Foundational completion
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - no dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational - independent of User Story 1 except for shared routing shell
- **User Story 3 (P3)**: Can start after Foundational - independent testability should use seeded active-account fixtures rather than depending on User Story 2 runtime flows

### Within Each User Story

- Tests MUST be written and FAIL before implementation unless the specification documents a justified exception
- Persistence and model support before services
- Services before controllers and pages
- Backend interfaces before frontend wiring
- Story-specific validation before moving to the next priority

### Parallel Opportunities

- T003, T004, and T005 can run in parallel during setup
- T010 through T019 can run in parallel during foundational work once T009 starts the backend/frontend skeleton
- T021 and T022 can run in parallel for User Story 1
- T028 through T031 can run in parallel for User Story 2 test coverage
- T032, T033, and T034 can run in parallel before service implementation for User Story 2
- T043 through T046 can run in parallel for User Story 3 test coverage
- T047, T048, and T049 can run in parallel before service implementation for User Story 3
- T058, T059, T060, T061, and T063 can run in parallel during polish

---

## Parallel Example: User Story 2

```bash
# Launch all User Story 2 tests together:
Task: "Create the registration and activation contract test in backend/src/test/java/com/winecellar/contracts/AuthRegistrationContractTest.java"
Task: "Create the registration and activation integration test in backend/src/test/java/com/winecellar/access/AuthRegistrationIntegrationTest.java"
Task: "Create the frontend registration flow test in frontend/tests/routes/registration-flow.test.tsx"
Task: "Create the identity event and activation email test in backend/src/test/java/com/winecellar/access/RegistrationAuditAndEmailTest.java"

# Launch the persistence layer tasks together:
Task: "Create the user account repository in backend/src/main/java/com/winecellar/access/repository/UserAccountRepository.java"
Task: "Create the account activation repository in backend/src/main/java/com/winecellar/access/repository/AccountActivationRepository.java"
Task: "Create the identity access event repository in backend/src/main/java/com/winecellar/access/repository/AccessEventRepository.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Stop and validate the public start page flow
5. Demo the public entry experience before moving to auth flows

### Incremental Delivery

1. Complete Setup + Foundational to establish backend/frontend infrastructure
2. Add User Story 1 and validate public entry
3. Add User Story 2 and validate registration + activation
4. Add User Story 3 and validate authenticated access to cellars
5. Finish with full-journey polish and regression coverage

### Suggested MVP Scope

The recommended MVP is **User Story 1** only after Setup and Foundational work.
It delivers a usable public entry point without committing yet to auth and
protected access behavior.

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to a specific user story for traceability
- Each user story is independently completable and testable
- Every phase ends with an explicit validation task before the next phase begins
- Keep domain terms aligned with the sitemap, contracts, and constitution
- Do not defer expiry handling, protected-route behavior, or auth regressions to a later phase
