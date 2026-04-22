# Data Model: CSV Bottle Import and Cellar View

## Information Model Scope

This feature covers CSV ingestion and bottle listing for a single default cellar
owned by one configured startup user. It does not implement registration,
login, or multi-user membership management.

## Entities

### `CONFIGURED_OWNER`

- Purpose: startup-provided actor identity for all operations in this feature
- Core fields:
  - `id`
  - `displayName`
- Source:
  - application configuration (`OWNER_ID`, `OWNER_DISPLAY_NAME`)
- Validation:
  - both fields required at startup or application fails fast

### `CELLAR`

- Purpose: storage container for imported bottles
- Core fields:
  - `id`
  - `name`
  - `createdAt`
- Validation:
  - exactly one default cellar is created/ensured for this feature

### `COMPARTMENT`

- Purpose: default storage location for imported bottles
- Core fields:
  - `id`
  - `cellarId`
  - `label`
  - `createdAt`
- Validation:
  - exactly one default compartment exists in the default cellar

### `PRODUCER`

- Purpose: canonical producer referenced by wine master records
- Core fields:
  - `id`
  - `name`
- Validation:
  - producer names normalized (trimmed, collapse repeated whitespace)

### `COUNTRY`

- Purpose: canonical country concept
- Core fields:
  - `id`
  - `name`

### `REGION`

- Purpose: canonical region concept
- Core fields:
  - `id`
  - `countryId`
  - `name`
- Validation:
  - region belongs to exactly one country

### `APPELLATION`

- Purpose: optional canonical appellation concept
- Core fields:
  - `id`
  - `regionId`
  - `name`
- Validation:
  - appellation belongs to exactly one region

### `BOTTLE_SIZE`

- Purpose: canonical bottle-size concept
- Core fields:
  - `id`
  - `label`
  - `volumeMl` nullable
- Validation:
  - size resolved by label from CSV `Size` column
- Nullable justification:
  - `volumeMl` may be null when label is known but numeric conversion is not
    yet available for all source values

### `WINE`

- Purpose: wine master data independent of physical inventory records
- Core fields:
  - `id`
  - `name`
  - `producerId`
  - `wineType`
  - `regionId` nullable
  - `appellationId` nullable
  - `varietal` nullable (scoped deviation)
- Validation:
  - exactly one of `regionId` or `appellationId` must be set
- Nullable justification:
  - `appellationId` null when CSV value is `Unknown`/empty and wine maps directly
    to region
  - `regionId` null when appellation is present
  - `varietal` null when source CSV field is empty

### `BOTTLE`

- Purpose: physical bottle inventory record
- Core fields:
  - `id`
  - `cellarId`
  - `compartmentId`
  - `wineId`
  - `bottleSizeId`
  - `vintage`
  - `quantity`
  - `pending`
  - `value`
  - `price`
  - `currency`
  - `consumeFromYear` nullable
  - `consumeUntilYear` nullable
  - `professionalScore` nullable
  - `communityScore` nullable
  - `createdAt`
- Validation:
  - `vintage=1001` means non-vintage and is rendered as NV in UI
  - bottle must always belong to both cellar and compartment
- Nullable justification:
  - consume window years null when CSV sentinel is `9999`
  - score fields null when CSV score columns are empty

### `EVENT`

- Purpose: immutable audit trail for imports
- Core fields:
  - `id`
  - `actorUserId`
  - `eventType` (`CSV_IMPORT`)
  - `occurredAt`
  - `primaryTargetType` (`CELLAR`)
  - `primaryTargetId`
  - `metadata` (`importedCount`, `skippedCount`, `fileName`)
- Validation:
  - one explicit primary target per event
  - event always includes actor and cellar context

## Relationships

- `CELLAR` 1 to many `COMPARTMENT`
- `CELLAR` 1 to many `BOTTLE`
- `COMPARTMENT` 1 to many `BOTTLE`
- `PRODUCER` 1 to many `WINE`
- `COUNTRY` 1 to many `REGION`
- `REGION` 1 to many `APPELLATION`
- `REGION` 1 to many `WINE` (when no appellation)
- `APPELLATION` 1 to many `WINE` (when present)
- `BOTTLE_SIZE` 1 to many `BOTTLE`
- `WINE` 1 to many `BOTTLE`
- `CONFIGURED_OWNER` 1 to many `EVENT`
- `CELLAR` 1 to many `EVENT`

## MongoDB Collection Design

### `cellars`

- document keys: `_id`, `name`, `createdAt`
- index: `{ name: 1 }`

### `compartments`

- document keys: `_id`, `cellarId`, `label`, `createdAt`
- indexes:
  - `{ cellarId: 1, label: 1 }` unique per cellar

### `producers`

- document keys: `_id`, `name`, `normalizedName`
- indexes:
  - `{ normalizedName: 1 }` unique

### `countries`

- document keys: `_id`, `name`, `normalizedName`
- indexes:
  - `{ normalizedName: 1 }` unique

### `regions`

- document keys: `_id`, `countryId`, `name`, `normalizedName`
- indexes:
  - `{ countryId: 1, normalizedName: 1 }` unique

### `appellations`

- document keys: `_id`, `regionId`, `name`, `normalizedName`
- indexes:
  - `{ regionId: 1, normalizedName: 1 }` unique

### `bottle_sizes`

- document keys: `_id`, `label`, `normalizedLabel`, `volumeMl`
- indexes:
  - `{ normalizedLabel: 1 }` unique

### `wines`

- document keys: `_id`, `name`, `normalizedName`, `producerId`, `wineType`,
  `regionId`, `appellationId`, `varietal`
- indexes:
  - `{ normalizedName: 1, producerId: 1, regionId: 1, appellationId: 1, varietal: 1 }`

### `bottles`

- document keys: `_id`, `cellarId`, `compartmentId`, `wineId`, `bottleSizeId`,
  `vintage`, `quantity`, `pending`, `value`, `price`, `currency`,
  `consumeFromYear`, `consumeUntilYear`, `professionalScore`, `communityScore`,
  `createdAt`
- indexes:
  - `{ cellarId: 1, createdAt: -1 }`
  - `{ cellarId: 1, wineId: 1, vintage: 1 }`

### `events`

- document keys: `_id`, `actorUserId`, `eventType`, `occurredAt`,
  `primaryTargetType`, `primaryTargetId`, `metadata`
- indexes:
  - `{ primaryTargetType: 1, primaryTargetId: 1, occurredAt: -1 }`
  - `{ actorUserId: 1, occurredAt: -1 }`

## State Transitions

### Import lifecycle

1. API receives CSV file.
2. Rows are parsed and validated.
3. Canonical reference entities are resolved/upserted.
4. `WINE` and `BOTTLE` records are persisted for valid rows.
5. Import summary is returned with imported and skipped counts.
6. One `EVENT` record is appended with `CSV_IMPORT` and cellar target.
