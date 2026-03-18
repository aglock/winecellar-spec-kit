# Quickstart: Landing, Registration, Login, and Cellars

## Prerequisites

- Docker Engine with Docker Compose
- Java 21 and Maven only if you want to run the backend outside containers
- Node.js 22 only if you want to run the frontend outside containers

## Default Local Runtime

1. From `/workspaces/winecellar-spec-kit`, start the full stack:

```bash
docker compose up --build
```

2. Open the services:

- Frontend: `http://localhost:5173`
- Backend API: `http://localhost:8080`
- MongoDB: `mongodb://localhost:27017`

## Expected Compose Services

- `mongodb`: MongoDB 8 with a named data volume
- `backend`: Spring Boot 4 API with environment variables for Mongo and
  activation-link logging
- `frontend`: React + Vite + Tailwind 4 app pointed at the backend base URL

## Feature Smoke Test

1. Open `/`
2. Confirm the landing page shows primary actions for `Register` and `Sign In`
3. Register a new account at `/register`
4. Inspect the backend application log and copy the logged activation link
5. Follow the activation link at `/activate`
6. Sign in at `/sign-in`
7. Confirm `/cellars` requires authentication and shows either cellar cards or
   the empty state

## Optional Split Runtime

### Backend

```bash
cd backend
mvn spring-boot:run
```

The first iterations should log activation links in the backend log output.
Brevo integration is planned later behind the same delivery interface.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Verification Commands

### Backend

```bash
cd backend
mvn test
```

### Frontend

```bash
cd frontend
npm test
npm run lint
```

### Compose

```bash
docker compose config
```
