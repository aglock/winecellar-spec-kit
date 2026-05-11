# Quickstart: OpenTelemetry Application Monitoring

## Prerequisites

- Java 21
- Maven 3.9+
- Docker with Docker Compose

## 1. Start Local Infrastructure

From repository root:

```bash
docker compose -f docker/mongo-compose.yml up -d
docker compose -f docker/observability-compose.yml up -d
```

## 2. Run Backend with OTLP Export

Use environment variables (example local values):

```bash
export SPRING_PROFILES_ACTIVE=dev
export SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/winecellar"
export MANAGEMENT_OTLP_TRACING_ENDPOINT="http://localhost:4318/v1/traces"
export MANAGEMENT_OTLP_METRICS_EXPORT_URL="http://localhost:4318/v1/metrics"
```

Start backend:

```bash
cd backend
mvn spring-boot:run
```

## 3. Verify Operational Endpoints

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/prometheus | head
```

## 4. Generate Sample Traffic

```bash
curl -X POST -F "file=@docs/resources/cellar-data.csv" http://localhost:8080/api/imports/wine-bottles
curl "http://localhost:8080/api/cellars/default/bottles?page=0&size=20"
```

## 5. Validate Telemetry

- Jaeger UI: http://localhost:16686
- Prometheus UI: http://localhost:9090

Suggested checks:

1. Confirm traces appear for import and bottle-list operations.
2. Confirm latency/error/throughput metrics are present.
3. Stop Collector temporarily and verify backend requests still succeed.

## 6. Security and Access Scope

- Dashboard/UI access must be limited to authenticated operations/admin users.
- Initial rollout scope is backend API observability in dev, staging, and production.

## 7. Phase Test Gates

```bash
# Backend automated tests
cd backend
mvn test

# Compose contract validity
cd ..
docker compose -f docker/observability-compose.yml config
```
