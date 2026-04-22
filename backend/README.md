# Winecellar Backend (Spring Boot + MongoDB)

## Prerequisites

- Java 21
- Maven 3.9+
- Docker + Docker Compose

## Configuration

Set environment variables before running:

- `SPRING_DATA_MONGODB_URI` (default: `mongodb://localhost:27017/winecellar`)
- `OWNER_ID` (default: `owner-default`)
- `OWNER_DISPLAY_NAME` (default: `Cellar Owner`)

## Run MongoDB

From repository root:

```bash
docker compose -f docker/mongo-compose.yml up -d
```

## Run Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`.

## Tests

```bash
cd backend
mvn test
```
