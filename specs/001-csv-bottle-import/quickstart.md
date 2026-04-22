# Quickstart: CSV Bottle Import and Cellar View

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 18+
- Docker and Docker Compose

## 1. Start MongoDB in Docker

From repository root:

```bash
docker compose -f docker/mongo-compose.yml up -d
```

## 2. Configure Backend Owner Identity

Set startup owner configuration (example):

```bash
export OWNER_ID=owner-default
export OWNER_DISPLAY_NAME="Cellar Owner"
export SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/winecellar"
export API_CORS_ALLOWED_ORIGINS="http://localhost:5173"
```

## 3. Run Backend (Maven + Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

Backend should start on `http://localhost:8080`.

## 4. Run Frontend (React + Vite + Tailwind)

```bash
cd frontend
npm install
npm run dev
```

Frontend should start on `http://localhost:5173`.

## 5. Test CSV Import API Manually

```bash
curl -X POST \
  -F "file=@docs/resources/cellar-data.csv" \
  http://localhost:8080/api/imports/wine-bottles
```

Expected response fields:

- `importedCount`
- `skippedCount`
- `skippedRows[]`
- `eventId`

## 6. Test Bottle List API

```bash
curl "http://localhost:8080/api/cellars/default/bottles?page=0&size=20"
```

## 7. Phase Test Gates

Run these before moving between phases/major milestones:

```bash
# backend
cd backend
mvn test

# frontend
cd ../frontend
npm test
npm run build

# optional cleanup
cd ..
docker compose -f docker/mongo-compose.yml down
```

## UX Conformance Checklist

- Use `design/design-system.json` token palette and typography.
- Keep import form labels persistent and explicit.
- Show success/error/empty states with calm, corrective copy.
- Render list rows with clear hierarchy: wine name, producer, vintage/NV,
  geography, size, quantity.
