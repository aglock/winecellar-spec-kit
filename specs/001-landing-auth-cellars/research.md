# Research: Landing, Registration, Login, and Cellars

## Decision: Separate backend and frontend applications orchestrated by Docker Compose

**Rationale**: The feature requires a Spring Boot API, a React web client, a
MongoDB instance, and local email capture. Separate applications keep build
pipelines, test suites, and container images simple while still allowing one
command local startup through `docker compose up --build`.

**Alternatives considered**:

- Monolithic Spring Boot app serving built frontend assets: rejected because it
  slows frontend iteration and couples deployment concerns too early.
- Frontend-only mock implementation: rejected because registration, activation,
  session expiry, and cellar authorization are backend behaviors.

## Decision: Use MongoDB collections aligned to aggregate boundaries instead of one denormalized auth document

**Rationale**: Registration, activation tokens, sessions, memberships, and
  cellar summaries change at different rates and need different indexes and TTL
  behavior. Separate collections allow explicit lifecycle rules, especially for
  activation expiry and session expiry, without collapsing identity and cellar
  concerns.

**Alternatives considered**:

- Single `users` document embedding activation token and active sessions:
  rejected because token lifecycle and session cleanup become harder to index and
  expire cleanly.
- Fully relational modeling in MongoDB-style references for every read:
  rejected because cellar list responses benefit from small denormalized summary
  fields on memberships.

## Decision: Persist activation tokens with issued-at, expires-at, and consumed-at fields

**Rationale**: Single-use activation links with a 15-minute validity window need
explicit validation for both time expiry and replay prevention. Keeping
`consumedAt` separate from deletion preserves auditability and allows clearer
user feedback for expired versus already-used links.

**Alternatives considered**:

- Delete token immediately on successful activation: rejected because it loses
  traceable evidence of token use.
- Store only a boolean `used`: rejected because it does not capture when the
  activation was consumed.

## Decision: Persist authenticated sessions server-side in MongoDB with 12-hour expiry

**Rationale**: The feature requires deterministic 12-hour session enforcement
and server-side invalidation on sign-out or revocation. MongoDB-backed sessions
allow explicit expiry checks, audit correlation, and Compose-friendly local
development without external session infrastructure.

**Alternatives considered**:

- Stateless JWT-only auth: rejected because explicit sign-out and expiry-event
  recording become harder without server-side session state.
- In-memory sessions: rejected because they break in container restarts and do
  not represent production behavior.

## Decision: Use HTTP-only secure cookies for the web session transport

**Rationale**: The frontend is a browser-based React app. HTTP-only cookies keep
session secrets out of JavaScript while still allowing the frontend to discover
auth state via a dedicated session endpoint.

**Alternatives considered**:

- Local storage bearer tokens: rejected because they increase XSS exposure.
- Basic auth for initial implementation: rejected because it does not represent
  the intended product login flow.

## Decision: Mirror the `frontend-template` route shell and component layering in a new `/frontend` app

**Rationale**: The template already demonstrates the desired route-first shape
for landing, sign-in, and cellars pages. Reusing that structure reduces design
drift while still allowing a proper TypeScript setup, Tailwind 4 syntax, and
design-token implementation from `design/design-system.json`.

**Alternatives considered**:

- Copy the template verbatim: rejected because it is JavaScript, uses Tailwind
  3-era setup, and contains demo-only behavior and routes.
- Build an unrelated folder structure: rejected because the user explicitly
  asked to mimic the template in a dedicated `frontend` folder.

## Decision: Translate the design system into frontend tokens and a small shared component set

**Rationale**: The design system defines colors, typography, spacing, motion,
page templates, and component states. Converting those rules into Tailwind 4
theme tokens plus shared `Button`, `Input`, `Card`, `AuthShell`, and
`AppHeader` components is the cleanest way to keep landing, auth, and cellar
pages visually consistent.

**Alternatives considered**:

- Styling each page independently: rejected because it would break consistency.
- Using `docs/ui-inspiration/*`: rejected because the planning prompt forbids
  those obsolete references.

## Decision: Design email delivery around a provider port, but log activation links in early iterations

**Rationale**: The long-term provider target is Brevo, but the first iterations
should stay operational without external email infrastructure. A dedicated
activation-delivery port allows iteration 1 to log activation links to the
application log while preserving a clean seam for a later `BrevoActivationDelivery`
adapter.

**Alternatives considered**:

- Adding Brevo immediately: rejected because it introduces external setup and
  credential management before the auth flow itself is proven.
- Mailpit in Docker Compose: rejected for the first iterations because the user
  prefers log-based activation delivery before introducing even local email
  infrastructure.
- Hard-coding log delivery without an abstraction: rejected because it would
  create avoidable rework when Brevo is introduced.

## Decision: Treat business-logic unit tests as mandatory deliverables in every implementation phase

**Rationale**: Registration, activation, session handling, route protection,
and cellar authorization are core business behavior. Requiring unit tests in
every implementation phase keeps those rules explicit and prevents "we will add
tests later" drift.

**Alternatives considered**:

- Integration tests only: rejected because they do not isolate business rules
  well enough and make regressions harder to localize.
- Adding unit tests at the end: rejected because gaps compound across phases.

## Decision: Return cellar list entries from a membership-centric read model

**Rationale**: The `/cellars` page only needs the cellars accessible to the
current user. Querying memberships by `userId` and resolving the corresponding
cellar summary minimizes authorization mistakes and keeps the read path aligned
with the constitution's cellar-level access model.

**Alternatives considered**:

- Querying all cellars then filtering client-side: rejected because it is a
  security anti-pattern.
- Returning full cellar documents: rejected because the page currently needs
  only summary data.

## Decision: Treat identity audit events as a temporary constitutional exception

**Rationale**: The feature spec requires registration and sign-in audit history
before cellar context exists. The current constitution restricts primary event
targets to `BOTTLE`, `COMPARTMENT`, or `CELLAR`, so this feature needs an
approved exception or a constitution amendment to support `USER_ACCOUNT` as the
primary target for identity events.

**Alternatives considered**:

- Skip identity events until the user owns a cellar: rejected because it fails
  the feature requirements.
- Force identity events into a fake cellar context: rejected because it would
  distort the domain model.
