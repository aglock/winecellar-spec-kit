# Data Model: Landing, Registration, Login, and Cellars

## Scope

This data model covers only the aggregates needed for the first feature slice:
public account registration, email activation, authenticated sessions,
identity access-history events, and the authenticated cellars landing page.

The model follows the information model in
`docs/architecture/information-model.mmd` while adapting it for MongoDB 8 using
document-database principles:

- separate aggregates for independently changing concepts
- embedding only where lifecycle and write ownership are aligned
- explicit references where relationships cross aggregate boundaries
- no collapse of distinct domain concepts for storage convenience

## Aggregate Overview

### USER_ACCOUNT

Represents an individual user who can register, activate an account, sign in,
and access cellars through memberships.

**Collection**: `user_accounts`

**Fields**:

- `id`: string, primary identifier
- `username`: string, unique login/display identifier if used by the product
- `email`: string, unique normalized email address
- `passwordHash`: string, derived from the user-provided password and stored
  instead of the plaintext password
- `displayName`: string
- `status`: enum `PENDING_ACTIVATION | ACTIVE | DISABLED`
- `createdAt`: datetime
- `activatedAt`: datetime nullable, justified because the account may remain
  pending until activation completes

**Validation rules**:

- `email` must be unique
- `passwordHash` must always be present for password-based accounts
- plaintext passwords must never be persisted in `USER_ACCOUNT`
- `status` must start as `PENDING_ACTIVATION` after successful registration
- `activatedAt` must be present only when `status = ACTIVE`

### ACCOUNT_ACTIVATION

Represents a single activation link issuance for a pending account.

**Collection**: `account_activations`

**Fields**:

- `id`: string, primary identifier
- `userAccountId`: string, reference to `USER_ACCOUNT`
- `tokenHash`: string
- `issuedAt`: datetime
- `expiresAt`: datetime
- `usedAt`: datetime nullable, justified because the link may expire unused
- `status`: enum `PENDING | USED | EXPIRED | SUPERSEDED`

**Validation rules**:

- `expiresAt` must equal `issuedAt + 15 minutes`
- only one valid pending activation may exist per user account at a time
- `usedAt` must be present only when `status = USED`

### AUTH_SESSION

Represents a successful authenticated session for an active account.

**Collection**: `auth_sessions`

**Fields**:

- `id`: string, primary identifier
- `userAccountId`: string, reference to `USER_ACCOUNT`
- `issuedAt`: datetime
- `expiresAt`: datetime
- `revokedAt`: datetime nullable, justified because a session may expire
  naturally without manual revocation
- `status`: enum `ACTIVE | EXPIRED | REVOKED`

**Validation rules**:

- `expiresAt` must equal `issuedAt + 12 hours`
- only active accounts may receive new sessions
- access checks must reject sessions when current time is after `expiresAt`

### ACCESS_EVENT

Represents an auditable identity/access history record for a single completed
state-changing auth action.

**Collection**: `access_events`

**Fields**:

- `id`: string, primary identifier
- `actorUserId`: string nullable, justified because pre-activation or failed
  sign-in events may not always resolve to an active account identifier
- `eventType`: enum `ACCOUNT_REGISTERED | ACTIVATION_ISSUED | ACCOUNT_ACTIVATED | SIGN_IN_SUCCEEDED | SIGN_IN_FAILED | SESSION_ENDED`
- `targetType`: enum `USER_ACCOUNT | ACCOUNT_ACTIVATION | AUTH_SESSION`
- `targetId`: string
- `occurredAt`: datetime
- `metadata`: object nullable, justified because some event types need details
  such as email address, expiry reason, or failure category while others do not

**Validation rules**:

- every event must have exactly one primary target through `targetType` and
  `targetId`
- `occurredAt` must be immutable after creation
- `metadata` must not duplicate canonical domain concepts already modeled

### CELLAR

Represents a cellar that may later contain inventory and structure. For this
feature, only summary fields are required for the cellars landing page.

**Collection**: `cellars`

**Fields**:

- `id`: string, primary identifier
- `name`: string
- `location`: string nullable, justified because the information model allows a
  cellar to exist without a physical location description
- `description`: string nullable, justified because summary text is optional
- `createdAt`: datetime

### CELLAR_MEMBERSHIP

Connects a user to a cellar with an explicit cellar-level role.

**Collection**: `cellar_memberships`

**Fields**:

- `id`: string, primary identifier
- `userAccountId`: string, reference to `USER_ACCOUNT`
- `cellarId`: string, reference to `CELLAR`
- `role`: enum `OWNER | CONTRIBUTOR | VIEWER`
- `grantedAt`: datetime
- `grantedByUserId`: string nullable, justified because the very first owner
  relationship may be system-created during cellar creation in a later feature

**Validation rules**:

- `role` must always be present
- the same user must not have duplicate active memberships for the same cellar

## Query Shapes

### Registration

- Derive `passwordHash` from the submitted password
- Create `USER_ACCOUNT` with `status = PENDING_ACTIVATION`
- Create `ACCOUNT_ACTIVATION` with 15-minute expiry
- Create `ACCESS_EVENT` records for `ACCOUNT_REGISTERED` and
  `ACTIVATION_ISSUED`

### Activation

- Look up activation by token
- Validate `status = PENDING` and `expiresAt > now`
- Mark activation as `USED`
- Update `USER_ACCOUNT.status` to `ACTIVE`
- Create `ACCESS_EVENT` for `ACCOUNT_ACTIVATED`

### Login

- Validate account is `ACTIVE`
- Verify the submitted password against `USER_ACCOUNT.passwordHash`
- Create `AUTH_SESSION` with `expiresAt = issuedAt + 12 hours`
- Create `ACCESS_EVENT` for `SIGN_IN_SUCCEEDED` or `SIGN_IN_FAILED`

### Cellars Landing Page

- Resolve current session to `USER_ACCOUNT`
- Query `CELLAR_MEMBERSHIP` by `userAccountId`
- Fetch matching `CELLAR` summaries by referenced IDs
- Return empty state when membership query returns no results

### Session End

- Resolve session end by expiry, revocation, or explicit sign-out
- Update `AUTH_SESSION.status`
- Create `ACCESS_EVENT` for `SESSION_ENDED`

## Event Considerations

The broader constitution requires history to be first-class. This feature
implements identity/access audit history through `ACCESS_EVENT` while leaving
cellar-domain events separate from the information model’s `EVENT` entity.
