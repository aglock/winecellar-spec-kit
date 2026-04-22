# Feature Specification: CSV Bottle Import and Cellar View

**Feature Branch**: `001-csv-bottle-import`  
**Created**: 2026-04-21  
**Status**: Draft  
**Input**: User description: "I want to build an application so that I can upload my csv file with wine bottles and show them."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Import Wine Bottles from CSV (Priority: P1)

A cellar owner uploads a CSV file exported from their wine management software.
The system reads the file, parses each row as a wine bottle record, links each
bottle to its canonical wine identity, and stores all successfully parsed bottles
in the cellar's default compartment. The owner can then see the imported bottles
immediately in the cellar list.

**Why this priority**: This is the core value of the feature. Without the ability
to import bottles, there is nothing to display. It is the entry point that makes
the rest of the application useful.

**Independent Test**: Can be fully tested by uploading a valid CSV file and
verifying that the resulting bottle count matches the number of data rows in
the file, independent of any list-view styling or UI refinement.

**Acceptance Scenarios**:

1. **Given** a CSV file in the expected format with valid rows, **When** the user
   uploads the file, **Then** all valid rows are stored as bottle inventory
   records and the user receives confirmation of how many bottles were imported.
2. **Given** a CSV file where some rows are malformed or missing required fields,
   **When** the user uploads the file, **Then** valid rows are imported and the
   user receives a list of skipped rows with the reason for each.
3. **Given** an empty CSV file or a file with only a header row, **When** the
   user uploads it, **Then** the system reports that no bottles were found and
   no records are created.
4. **Given** a file that is not a CSV (wrong format or extension), **When** the
   user attempts to upload it, **Then** the system rejects it before processing
   with a clear, human-readable error message.

---

### User Story 2 - View Cellar Bottle List (Priority: P2)

A cellar owner opens the application and sees a list of all bottles currently
stored in the cellar. Each row shows the key identity and provenance details
for that bottle so the owner can understand what they have at a glance.

**Why this priority**: Viewing the list delivers the value of having imported the
data. However, without any imported bottles, the list is empty, so the import
story must be working first.

**Independent Test**: Can be tested independently by pre-loading bottle records
directly (without the CSV flow) and verifying that the list renders them
correctly, confirming that the list view delivers value on its own.

**Acceptance Scenarios**:

1. **Given** the cellar contains imported bottles, **When** the owner opens the
   bottle list, **Then** all bottles are displayed with wine name, vintage,
   producer, colour/type category, bottle size, country, region, and quantity.
2. **Given** the cellar contains no bottles, **When** the owner opens the bottle
   list, **Then** the list shows a clear empty state message prompting the user
   to import a CSV file.
3. **Given** the bottle list is open after a successful import, **When** the
   owner views it without refreshing, **Then** the newly imported bottles appear
   immediately.

---

### Edge Cases

- What happens when the CSV file contains duplicate rows (same wine, same
  vintage, same producer)? Each row that is valid MUST be stored as a separate
  bottle record; deduplication is out of scope for this feature.
- How does the system handle CSV files using non-standard delimiters or
  character encodings? The system MUST document the expected CSV format and
  reject files that cannot be parsed accordingly.
- What happens when a vintage value is `1001` (as used in the reference data
  for non-vintage wines)? The system MUST treat `1001` as a special sentinel
  meaning "non-vintage" (NV) and display it accordingly in the list, not as a
  year.
- What happens when `Appellation` is `"Unknown"` in the CSV? The system MUST
  treat `"Unknown"` as absent and leave the `APPELLATION` link null, linking the
  bottle directly to `REGION` instead.
- What happens when `SubRegion` is `"Unknown"` in the CSV? The system MUST
  treat `"Unknown"` as absent for the same reason.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST accept a CSV file upload matching the format
  defined in `docs/resources/cellar-data.csv`, including the column set:
  `Type`, `Color`, `Category`, `Size`, `Currency`, `Value`, `Price`,
  `TotalQuantity`, `Quantity`, `Pending`, `Vintage`, `Wine`, `Locale`,
  `Producer`, `Varietal`, `Country`, `Region`, `SubRegion`, `Appellation`,
  `BeginConsume`, `EndConsume`, `PScore`, `CScore`.
- **FR-002**: The system MUST parse each CSV row and create one `BOTTLE`
  inventory record per valid row, linked to a `WINE` master data record. If an
  equivalent `WINE` record already exists (same name, producer, and vintage),
  the system MUST reuse it rather than creating a duplicate.
- **FR-003**: The system MUST validate each CSV row for required fields (`Wine`,
  `Producer`, `Vintage`, `Country`, `Region`) and reject individual rows that
  are missing these values, while continuing to process the remaining rows.
- **FR-004**: The system MUST store all successfully parsed bottles in the
  cellar's default compartment. For this feature, a single default cellar with
  a single default compartment is sufficient.
- **FR-005**: The system MUST create an audit event for each import operation,
  recording the acting user, event type (`CSV_IMPORT`), occurrence time, and
  the cellar as the primary target (`CELLAR`), along with the count of bottles
  imported and the count of rows skipped.
- **FR-006**: The system MUST display a list of all bottles in the cellar,
  showing at minimum: wine name, producer, vintage (with NV displayed for
  sentinel value `1001`), colour/type category, bottle size, country, and
  region.
- **FR-007**: The system MUST separate wine master data (`WINE`: name, producer
  reference, colour/type category, varietal, region or appellation) from bottle
  inventory records (`BOTTLE`: cellar, compartment, bottle size reference,
  vintage, quantity, value, price, consume window). Vintage MUST reside on
  `BOTTLE`, not on `WINE`, because the same wine identity can exist across
  multiple vintages. A `BOTTLE` record MUST reference a `WINE` record and a
  `BOTTLE_SIZE` record; it MUST NOT duplicate wine identity fields inline.
- **FR-008**: The system MUST use canonical `COUNTRY`, `REGION`, and optionally
  `APPELLATION` concepts. When `Appellation` in the CSV is `"Unknown"`, the
  bottle's wine MUST link directly to `REGION` and leave `APPELLATION` absent.
  When `SubRegion` is `"Unknown"`, it MUST be treated as absent.
- **FR-009**: The system MUST display a confirmation summary after import
  showing the count of bottles successfully imported and the count and
  descriptions of rows that were skipped.
- **FR-010**: User creation, registration, and login are out of scope for this
  feature. The application MUST be configured at startup with a single owner
  identity (display name and a stable identifier) supplied through application
  configuration (for example environment variables or a configuration file).
  This configured user is automatically the `OWNER` of the default cellar and
  the actor recorded on all audit events. No sign-in flow, password, or session
  management MUST exist. Full multi-user cellar membership is out of scope and
  MUST NOT be implemented as part of this feature.

### Key Entities

- **CELLAR**: The top-level storage unit. For this feature, one default cellar
  is pre-created. Owns all permissions for its contents.
- **COMPARTMENT**: A subdivision of a cellar. One default compartment is
  pre-created within the default cellar. All imported bottles are placed here.
- **BOTTLE**: One physical bottle in inventory. References the cellar, the
  compartment, the wine master data record, and a canonical bottle size. Carries
  physical attributes: vintage (integer; `1001` means non-vintage), bottle size,
  quantity, price, value, consume window, and bottle status. Vintage lives here,
  not on `WINE`, because the same wine identity may be represented by bottles of
  different vintages.
- **BOTTLE_SIZE**: Canonical bottle size descriptor (e.g. `750ml`, `1.5L`).
  Referenced by `BOTTLE`. Derived from the `Size` CSV column.
- **WINE**: Wine master data representing a wine identity independent of any
  physical bottle or vintage. Core attributes: name, wine type (colour
  category), references to `PRODUCER`, `REGION` (nullable when `APPELLATION` is
  set), and optional `APPELLATION`. Varietal blend detail is structurally
  modelled via `WINE_GRAPE` → `GRAPE`, but for this minimal import feature a
  single plain-text varietal field on `WINE` is used as a scoped deviation
  (see Assumptions).
- **PRODUCER**: Canonical producer entity. `WINE` references exactly one
  `PRODUCER`. Derived from the `Producer` CSV column.
- **COUNTRY**: Canonical geographic concept. Reached from `WINE` through
  `REGION`. Derived from the `Country` CSV column.
- **REGION**: Canonical geographic concept linked to exactly one `COUNTRY`.
  `WINE` links directly to `REGION` when no `APPELLATION` is set. Derived from
  the `Region` CSV column.
- **APPELLATION**: Optional canonical geographic concept linked to a `REGION`.
  When present, `WINE` links to `APPELLATION` and `region_id` on `WINE` is
  null. Derived from the `Appellation` CSV column when not `"Unknown"`.
- **CONFIGURED_OWNER**: A single user identity supplied through application
  configuration at startup (display name and stable identifier). Automatically
  holds the `OWNER` role for the default cellar. Not created through any
  registration flow; no password or session record exists.
- **IMPORT_EVENT**: Audit record created on each CSV import. Primary target is
  `CELLAR`. Records actor (the configured owner), event type `CSV_IMPORT`,
  timestamp, cellar context, bottles imported count, and rows skipped count.
  `CSV_IMPORT` is an extension of the canonical event type set for this feature.

#### Cardinalities

- `CELLAR` contains one or more `COMPARTMENT` (one default compartment for this
  feature).
- `COMPARTMENT` belongs to exactly one `CELLAR`.
- `BOTTLE` belongs to exactly one `CELLAR` and exactly one `COMPARTMENT`.
- `BOTTLE` references exactly one `WINE` and exactly one `BOTTLE_SIZE`.
- `WINE` references exactly one `PRODUCER`.
- `WINE` references either exactly one `APPELLATION` **or** exactly one `REGION`
  directly — never both, never neither.
- `APPELLATION` belongs to exactly one `REGION`.
- `REGION` belongs to exactly one `COUNTRY`.

#### Nullable Justifications

- `WINE.appellation_id`: Null when the CSV `Appellation` value is `"Unknown"` or
  empty. In that case, `WINE` links directly to `REGION` (per Principle V).
- `WINE.region_id`: Null when `APPELLATION` is present. The indirect path to
  country goes through `APPELLATION → REGION → COUNTRY`.
- `WINE.varietal` *(scoped deviation)*: The information model structures varietal
  data through `WINE_GRAPE` → `GRAPE`. For this minimal import feature, a single
  plain-text varietal field is stored on `WINE` to avoid building the full grape
  master data pipeline. This deviation MUST be resolved in a subsequent feature
  that aligns grape data with `docs/resources/grapes-masterlist.json`.
- `BOTTLE.consumeWindowBegin` and `BOTTLE.consumeWindowEnd`: In the reference
  CSV, the sentinel `9999` indicates "no recommendation". Both MUST be stored as
  null when the value is `9999`.
- `BOTTLE.professionalScore` and `BOTTLE.communityScore`: Absent when the CSV
  fields `PScore` and `CScore` are empty.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A user can upload a 500-row CSV file and see all imported bottles
  in the list within 30 seconds of submitting the file.
- **SC-002**: Every valid row in an uploaded CSV file results in a visible bottle
  record in the cellar list immediately after import completes.
- **SC-003**: Every invalid or skipped row is identified and reported in the
  import summary, with the row number and the reason for rejection.
- **SC-004**: The bottle list renders all stored bottles without requiring a page
  refresh after a successful import.
- **SC-005**: The bottle list remains readable and usable with at least 500
  entries without noticeable scrolling degradation.

## Assumptions

- The application starts with a single pre-created default cellar and a single
  default compartment. No user-facing cellar or compartment creation UI is
  required for this feature.
- Authentication, registration, and login are fully out of scope. The
  application is pre-configured at startup with a single owner identity
  (display name and stable identifier) via configuration. No user-facing
  sign-in screen, password management, or session handling exists in this
  feature. The configured owner is automatically the actor for all operations
  and the `OWNER` of the default cellar.
- The CSV format matches the structure in `docs/resources/cellar-data.csv`
  exactly, including column names and encoding. Files with different column
  sets MUST be rejected.
- `TotalQuantity` and `Quantity` are treated as equivalent for import purposes.
  Each CSV row represents one logical bottle entry (which may represent multiple
  physical bottles via quantity). One `BOTTLE` record is created per row.
- The `Value` and `Price` columns use the system's reference currency (`SEK` in
  the sample data). Currency handling beyond storage is out of scope.
- `BeginConsume` and `EndConsume` values of `9999` mean "no recommendation" and
  MUST be stored as null.
- The `Varietal` CSV column is stored as a single plain-text field on `WINE`
  rather than being resolved into the canonical `WINE_GRAPE` → `GRAPE` structure
  defined in the information model. This is a scoped deviation. A subsequent
  feature MUST replace this with proper grape entity resolution aligned to
  `docs/resources/grapes-masterlist.json`.
- `BOTTLE_SIZE` records are resolved or created by matching the `Size` CSV
  column value (e.g. `750ml`, `1.5L`) against canonical size labels. No manual
  size management UI is required.
- Full multi-user cellar sharing and membership management is explicitly out of
  scope and MUST be implemented in a subsequent feature.
