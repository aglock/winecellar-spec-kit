# Data Model: OpenTelemetry Application Monitoring

## Information Model Scope

This feature introduces an observability model for backend operations. It does
not change cellar business entities (`CELLAR`, `BOTTLE`, `WINE`, etc.) and does
not alter business persistence schema in MongoDB.

## Entities

### `OBSERVED_OPERATION`

- Purpose: logical unit of work representing an incoming backend API request
  (for example CSV import or bottle list retrieval).
- Core fields:
  - `operationName`
  - `startTime`
  - `endTime`
  - `durationMs`
  - `status` (`SUCCESS`, `ERROR`)
  - `environment` (`dev`, `staging`, `prod`)

### `TRACE`

- Purpose: correlated trace for one observed operation.
- Core fields:
  - `traceId`
  - `samplingDecision`
  - `spanCount`
  - `rootSpanName`
- Validation:
  - successful operations sampled at 10% by default
  - failed operations always sampled

### `SPAN`

- Purpose: timed sub-operation within a trace.
- Core fields:
  - `spanId`
  - `traceId`
  - `parentSpanId` nullable
  - `name`
  - `kind`
  - `startTime`
  - `endTime`
  - `status`
  - `attributes`
- Nullable justification:
  - `parentSpanId` is null for root spans

### `METRIC_SERIES`

- Purpose: time series representing operational health signals.
- Core fields:
  - `metricName`
  - `labels` (service, environment, route, outcome)
  - `aggregationType` (counter, histogram, gauge)
  - `points`
- Required series (minimum):
  - request throughput
  - request latency
  - request error count/rate

### `EXPORT_BUFFER_POLICY`

- Purpose: resiliency behavior for telemetry delivery during backend outages.
- Core fields:
  - `queueType` (`bounded-memory`)
  - `retryEnabled` (boolean)
  - `overflowPolicy` (`drop-oldest`)
  - `blockingMode` (`non-blocking`)

### `OBSERVABILITY_ACCESS_POLICY`

- Purpose: defines who may access telemetry UIs and dashboards.
- Core fields:
  - `allowedPrincipals` (`operations`, `admin`)
  - `authenticationRequired` (boolean)

## Relationships

- One `OBSERVED_OPERATION` has zero or one `TRACE`.
- One `TRACE` contains one or more `SPAN`.
- One `OBSERVED_OPERATION` contributes to one or more `METRIC_SERIES`.
- One `EXPORT_BUFFER_POLICY` governs many `TRACE` and `METRIC_SERIES` export attempts.
- One `OBSERVABILITY_ACCESS_POLICY` governs dashboard and telemetry UI access.

## Cardinalities

- `OBSERVED_OPERATION` 1 -> 0..1 `TRACE`
- `TRACE` 1 -> 1..* `SPAN`
- `OBSERVED_OPERATION` 1 -> 1..* `METRIC_SERIES`
- `EXPORT_BUFFER_POLICY` 1 -> * export events
- `OBSERVABILITY_ACCESS_POLICY` 1 -> * UI access checks

## State Transitions

1. Request enters backend and creates an `OBSERVED_OPERATION`.
2. Sampling policy determines whether a `TRACE` is emitted.
3. Spans are produced during operation execution.
4. Metrics are aggregated regardless of trace sampling.
5. Collector export pipeline attempts delivery.
6. On exporter outage, buffer policy applies retry and drop-oldest semantics while request processing remains non-blocking.
