# Research: OpenTelemetry Application Monitoring

## Decision 1: Use Spring Boot Actuator + Micrometer + OpenTelemetry Bridge

- Decision: Instrument backend APIs with Spring Boot Actuator, Micrometer observation, and OpenTelemetry OTLP export from the backend service.
- Rationale: This is the idiomatic Spring Boot 4.0.6 path, minimizes custom instrumentation code, and keeps metrics/traces aligned.
- Alternatives considered:
  - Direct OpenTelemetry SDK-only instrumentation: Rejected due to higher implementation complexity and duplication with Micrometer features.
  - Agent-only auto-instrumentation without app configuration: Rejected for lower control over signal naming and sampling policy.

## Decision 2: Collector-Centric Telemetry Pipeline

- Decision: Route all application telemetry to OpenTelemetry Collector; export traces to Jaeger and metrics for Prometheus scraping.
- Rationale: Collector centralizes sampling/export policy, supports backend resiliency controls, and decouples application code from backend tools.
- Alternatives considered:
  - Export directly from app to Jaeger and Prometheus: Rejected due to tighter coupling and weaker resiliency controls.
  - Use Jaeger-only stack: Rejected because metrics are mandatory in scope.

## Decision 3: Signal Scope Is Traces + Metrics (No Logs)

- Decision: Implement only traces and metrics for this feature increment.
- Rationale: Matches clarified scope, delivers incident triage value, and avoids broadening into log pipeline governance.
- Alternatives considered:
  - Add logs now: Rejected as out of scope and would increase operational/security work.
  - Metrics-only: Rejected because end-to-end correlation requires traces.

## Decision 4: Sampling and Failure Visibility Policy

- Decision: Use head-based probabilistic sampling at 10% for successful requests and always sample failed requests.
- Rationale: Meets overhead constraints while preserving error diagnostics.
- Alternatives considered:
  - 100% sampling: Rejected due to avoidable cost and overhead risk.
  - Tail sampling now: Rejected for unnecessary complexity at this stage.

## Decision 5: Export Resiliency Policy

- Decision: Use non-blocking export with bounded queue, retry, and drop-oldest overflow policy.
- Rationale: Keeps core API availability independent of telemetry backend outages.
- Alternatives considered:
  - Blocking request path until export succeeds: Rejected because it violates availability goals.
  - Unbounded queue: Rejected due to memory growth risk.

## Decision 6: Rollout and Access Scope

- Decision: Roll out backend API observability in dev, staging, and production; restrict dashboard/UI access to authenticated operations/admin users only.
- Rationale: Delivers cross-environment visibility while preserving operational security boundaries.
- Alternatives considered:
  - Production-only rollout: Rejected because rollout verification and calibration need lower environments.
  - Broad internal or public UI access: Rejected for security and least-privilege reasons.

## Decision 7: Contract Surface for This Feature

- Decision: Treat operational HTTP endpoints (`/actuator/health`, `/actuator/prometheus`) and OTLP ingest endpoints as the interface contract for observability.
- Rationale: These interfaces are what operators and platform tooling depend on and can be validated in automation.
- Alternatives considered:
  - No explicit contract artifact: Rejected because it weakens testability and cross-team integration clarity.
