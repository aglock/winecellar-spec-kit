# Research: Landing, Registration, Login, and Cellars

## Decision: Treat the frontend as React Native for Web delivered through Vite

**Rationale**: The product target in the spec and sitemap is a web application,
while the provided frontend stack says React Native with Vite. React Native for
Web is the most coherent way to satisfy both constraints without changing the
requested stack.

**Alternatives considered**:

- Plain React web components: rejected because it ignores the stated React
  Native requirement.
- Expo-native mobile application: rejected because the current feature scope,
  sitemap, and design brief are web-first.

## Decision: Use separate MongoDB collections for accounts, activation records, sessions, cellars, and memberships

**Rationale**: These concepts have different lifecycles, write patterns, and
authorization responsibilities. Keeping them as separate aggregates preserves
domain boundaries and avoids over-embedding data that changes independently.

**Alternatives considered**:

- Embedding memberships directly inside cellar documents: rejected for now
  because membership is queried by user and changes independently from cellar
  summary data.
- Embedding sessions directly inside user documents: rejected because session
  creation and expiry are write-heavy and benefit from isolated lifecycle
  management.

## Decision: Model activation as a single-use record with explicit expiry

**Rationale**: The feature requires a 15-minute activation link and single-use
semantics. A dedicated activation record keeps token lifecycle, expiry, and use
status explicit and auditable.

**Alternatives considered**:

- Storing activation state only on the user account: rejected because it does
  not model token issuance and invalidation cleanly.
- Reusing session storage for activation links: rejected because activation and
  authentication are separate domain concerns.

## Decision: Use a 12-hour server-validated authenticated session

**Rationale**: The feature requires an exact session duration. A server-side
session record with explicit expiry keeps validity and revocation behavior
traceable and consistent across protected routes.

**Alternatives considered**:

- Long-lived stateless tokens only: rejected because explicit session lifecycle
  control is clearer for the required duration-based behavior.
- Indefinite sessions until manual sign-out: rejected because it conflicts with
  the feature requirement.

## Decision: Expose minimal contracts for registration, activation, login, session inspection, and cellar listing

**Rationale**: This feature needs only the public auth entry points and the
authenticated cellar-list view. Keeping the contract small reduces accidental
scope creep while making testing straightforward.

**Alternatives considered**:

- Adding cellar creation and invitation APIs now: rejected because they are out
  of scope for the first feature slice.
- Using frontend-only mocks as the primary contract: rejected because backend
  auth and protected access are core feature requirements.

## Decision: Keep the cellars page focused on accessible cellar summaries and empty state

**Rationale**: The feature establishes authenticated entry into the cellar area,
not cellar management. The page should support users who already have
memberships and users who have none, without introducing create/share flows yet.

**Alternatives considered**:

- Folding cellar creation into this feature: rejected because the current spec
  bounds the first slice to fundamentals only.
- Requiring at least one cellar to complete registration: rejected because
  shared-cellar access may come later through invitation or owner workflows.

## Decision: Apply the UI design brief as a tone and layout system, not as a literal single-page inventory clone

**Rationale**: The design brief describes a premium dashboard shell and
consistent interaction language. The landing, registration, login, and cellars
pages should reuse that calm, structured, collector-oriented visual system while
adapting it to public/auth flows.

**Alternatives considered**:

- Using unrelated auth screens with a different visual system: rejected because
  it would violate UX consistency.
- Reproducing the inventory view layout unchanged on every page: rejected
  because public and auth flows need different information hierarchy.

## Decision: Require automated coverage across backend, frontend, contract, and end-to-end flows

**Rationale**: The constitution requires testing standards, and this feature
introduces authentication, expiry windows, protected routes, and role-aware
cellar access entry points. These behaviors are regression-prone and need
explicit coverage.

**Alternatives considered**:

- Manual testing only: rejected because time-based auth rules and protected
  access behavior need repeatable verification.
