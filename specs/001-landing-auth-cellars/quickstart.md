# Quickstart: Landing, Registration, Login, and Cellars

## Purpose

This quickstart describes how to implement and validate the first feature slice:
public landing page, registration and activation, login, and the authenticated
cellars landing page.

## Implementation Order

1. Build backend account registration, activation, login, and session
   validation flows.
2. Build backend identity access-history event creation for registration,
   activation, sign-in outcomes, and session end.
3. Build backend cellar-summary retrieval for the authenticated user.
4. Build frontend public routes for landing, registration, activation result,
   and login.
5. Build frontend protected route handling and the authenticated cellars page.
6. Wire frontend service calls to the backend contracts.
7. Add automated tests for backend, frontend, contract, end-to-end, and
   performance verification flows.

## Suggested Validation Passes

### Phase 0 Validation

- Confirm `research.md` resolves stack, MongoDB modeling, and UX decisions.

### Phase 1 Validation

- Confirm `data-model.md` matches the information model and constitution.
- Confirm the OpenAPI contract covers registration, activation, login, session
  inspection, and cellar list retrieval.
- Confirm route decisions are fixed for `/register`, `/activate`, `/sign-in`,
  and `/cellars`.
- Confirm identity access-history events are defined for registration,
  activation, sign-in outcomes, and session end.
- Confirm the contract does not exceed the feature scope.

### Implementation Validation

- Registration creates a pending account and issues a 15-minute activation link.
- Registration triggers both access-history event creation and activation email
  dispatch.
- Activation succeeds only once and fails after expiry.
- Activation records an access-history event.
- Login succeeds only for active accounts.
- Login success and failure record access-history events.
- Successful login creates a 12-hour session.
- Session end records an access-history event.
- Unauthenticated access to the cellars page is denied.
- Authenticated users with no memberships see the expected empty state.
- Authenticated users with memberships see accessible cellar summaries only.
- Public and authenticated routes resolve at `/`, `/register`, `/activate`,
  `/sign-in`, and `/cellars`.
- Public page rendering and auth/cellar requests meet the performance targets in
  `plan.md`.

## Planned Test Commands

These commands are the expected validation targets once the application
structure exists:

```bash
# Backend
./mvnw test

# Frontend
npm test

# Contract / end-to-end
npm run test:e2e
```

All tests for the current phase must pass before progressing to the next phase.
