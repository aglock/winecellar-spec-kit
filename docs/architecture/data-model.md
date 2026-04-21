# Data Model: Landing, Registration, Login, and Cellars

## Information Model Scope

This feature covers identity and access foundations plus the authenticated
cellar landing page. It reads cellar collaboration data through
`CELLAR_MEMBERSHIP` but does not create or modify cellar-sharing rules.

## Entities

### `USER_ACCOUNT`

- Purpose: person who can register, activate, sign in, and access cellars
- Core fields:
  - `id`
  - `email`
  - `passwordHash`
  - `displayName`
  - `status` = `PENDING_ACTIVATION | ACTIVE | DISABLED`
  - `createdAt`
  - `activatedAt` nullable
- Validation:
  - `email` must be unique and normalized
  - `passwordHash` is required after successful registration
  - `activatedAt` is required when `status = ACTIVE`
- Nullable justification:
  - `activatedAt` is null while the account is pending activation

### `ACCOUNT_ACTIVATION`

- Purpose: single-use activation grant for one pending account
- Core fields:
  - `id`
  - `userAccountId`
  - `tokenHash`
  - `issuedAt`
  - `expiresAt`
  - `consumedAt` nullable
  - `deliveryStatus`
- Validation:
  - exactly one active unused token per pending account at a time
  - `expiresAt = issuedAt + 15 minutes`
  - token cannot be consumed after expiry
- Nullable justification:
  - `consumedAt` is null until the link is used

### `AUTHENTICATED_SESSION`

- Purpose: server-side representation of a signed-in browser session
- Core fields:
  - `id`
  - `userAccountId`
  - `issuedAt`
  - `expiresAt`
  - `endedAt` nullable
  - `endReason` nullable = `SIGN_OUT | EXPIRED | REVOKED`
  - `clientMetadata`
- Validation:
  - `expiresAt = issuedAt + 12 hours`
  - session is valid only when `endedAt` is null and `expiresAt` is in the
    future
- Nullable justification:
  - `endedAt` and `endReason` are null while the session remains active

### `CELLAR`

- Purpose: shared cellar container already defined in the canonical information
  model; used here only as a readable destination for signed-in users
- Core fields used by this feature:
  - `id`
  - `name`
  - `location`
  - `description`
  - `createdAt`

### `CELLAR_MEMBERSHIP`

- Purpose: cellar-level access grant linking one user to one cellar with one
  explicit role
- Core fields:
  - `id`
  - `userId`
  - `cellarId`
  - `role` = `OWNER | CONTRIBUTOR | VIEWER`
  - `grantedAt`
  - `grantedByUserId`
- Validation:
  - unique pair of `userId + cellarId`
  - role is required and governs cellar visibility

### `CELLAR_SUMMARY`

- Purpose: read model returned to the `/cellars` page
- Core fields:
  - `cellarId`
  - `name`
  - `location`
  - `description`
  - `memberRole`
  - `bottleCount` nullable
  - `recentActivityAt` nullable
- Validation:
  - returned only when a matching `CELLAR_MEMBERSHIP` exists
- Nullable justification:
  - `bottleCount` can be absent until inventory counting is implemented
  - `recentActivityAt` can be absent when no cellar activity exists yet

### `IDENTITY_ACCESS_EVENT`

- Purpose: immutable audit trail for registration, activation, sign-in, and
  session termination
- Core fields:
  - `id`
  - `actorUserId` nullable for unauthenticated attempts
  - `subjectUserAccountId`
  - `eventType` =
    `ACCOUNT_REGISTERED | ACTIVATION_ISSUED | ACTIVATION_SUCCEEDED | ACTIVATION_REJECTED | SIGN_IN_SUCCEEDED | SIGN_IN_FAILED | SESSION_ENDED`
  - `occurredAt`
  - `primaryTargetType`
  - `primaryTargetId`
  - `metadata`
- Validation:
  - one explicit primary target per event
  - `SESSION_ENDED` metadata includes `endReason`
- Nullable justification:
  - `actorUserId` is null for unauthenticated registration and failed login
    attempts before identity is established
- Constitutional note:
  - this feature needs an approved exception or amendment so
    `primaryTargetType = USER_ACCOUNT` is valid for identity events

## Relationships

- `USER_ACCOUNT` 1 to many `ACCOUNT_ACTIVATION`
- `USER_ACCOUNT` 1 to many `AUTHENTICATED_SESSION`
- `USER_ACCOUNT` 1 to many `CELLAR_MEMBERSHIP`
- `CELLAR` 1 to many `CELLAR_MEMBERSHIP`
- `CELLAR_MEMBERSHIP` many to 1 `CELLAR`
- `CELLAR_SUMMARY` is derived from `CELLAR` joined with `CELLAR_MEMBERSHIP`
- `USER_ACCOUNT` 1 to many `IDENTITY_ACCESS_EVENT` as subject

## Role Model And Permission Effects

This feature touches collaboration because cellar visibility is membership-based.

- `OWNER`: can view `/cellars`; cellar cards returned for owned cellars
- `CONTRIBUTOR`: can view `/cellars`; cellar cards returned for contributed
  cellars
- `VIEWER`: can view `/cellars`; cellar cards returned for viewed cellars
- Sharing behavior is unchanged in this feature; no new membership mutation is
  introduced

## Event Model

| Event Type | When Created | Primary Target | Required Metadata |
|------------|--------------|----------------|-------------------|
| `ACCOUNT_REGISTERED` | Pending account created | `USER_ACCOUNT` | normalized email |
| `ACTIVATION_ISSUED` | Activation token persisted and email dispatch requested | `USER_ACCOUNT` | activation id, expires at |
| `ACTIVATION_SUCCEEDED` | Valid activation link consumed | `USER_ACCOUNT` | activation id |
| `ACTIVATION_REJECTED` | Expired, invalid, or reused link submitted | `USER_ACCOUNT` when known, otherwise token hash | rejection reason |
| `SIGN_IN_SUCCEEDED` | Credentials accepted and session issued | `USER_ACCOUNT` | session id, expires at |
| `SIGN_IN_FAILED` | Credentials rejected or account inactive | `USER_ACCOUNT` when known, otherwise normalized email | rejection reason |
| `SESSION_ENDED` | Sign-out, expiry, or revocation ends a session | `USER_ACCOUNT` | session id, end reason |

## MongoDB Collection Design

### `user_accounts`

- Aggregate root: `USER_ACCOUNT`
- Indexes:
  - unique `email`
  - `status, email`

### `account_activation_tokens`

- Aggregate root: `ACCOUNT_ACTIVATION`
- Indexes:
  - unique `userAccountId` for active unused token enforcement
  - `expiresAt`
  - unique `tokenHash`
- Lifecycle:
  - expired rows may be cleaned asynchronously after they are no longer needed
    for audit lookups

### `authenticated_sessions`

- Aggregate root: `AUTHENTICATED_SESSION`
- Indexes:
  - `userAccountId, expiresAt`
  - unique session identifier
  - TTL-compatible cleanup index on `expiresAt` or `endedAt`

### `cellars`

- Existing aggregate root from canonical model
- Read pattern for this feature:
  - resolve cellar summaries by membership-owned `cellarId`

### `cellar_memberships`

- Aggregate root: `CELLAR_MEMBERSHIP`
- Indexes:
  - unique `userId, cellarId`
  - `userId, role`
  - `cellarId, role`
- Denormalization option:
  - store a small cellar summary snapshot for faster `/cellars` reads if needed
    later; do not denormalize now without measurement

### `identity_access_events`

- Aggregate root: `IDENTITY_ACCESS_EVENT`
- Indexes:
  - `subjectUserAccountId, occurredAt desc`
  - `eventType, occurredAt desc`

## State Transitions

### Account lifecycle

1. Registration creates `USER_ACCOUNT(status = PENDING_ACTIVATION)`
2. Activation link validation within 15 minutes marks activation consumed
3. Account becomes `ACTIVE`

### Session lifecycle

1. Successful sign-in creates `AUTHENTICATED_SESSION`
2. Session remains valid until sign-out, revocation, or 12-hour expiry
3. Ended session records `endedAt` and `endReason`

## Read Models

### Cellars page

- Query memberships by current `userId`
- Join to `cellars`
- Return ordered `CELLAR_SUMMARY[]`
- If no memberships exist, return an empty list and let the frontend show the
  empty state

## Performance Notes

- `/cellars` must use indexed membership lookup first, not full-cellar scans
- session validation should resolve with one indexed session lookup and one
  optional account status lookup
- registration and sign-in event writes should be append-only and non-blocking
  relative to response generation where practical
