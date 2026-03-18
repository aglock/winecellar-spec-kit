# winecellar-spec-kit Development Guidelines

Auto-generated from all feature plans. Last updated: 2026-03-17

## Active Technologies
- Java 21 for backend services; TypeScript 5.x for frontend application + Spring Boot 4, Spring Security, Spring Data MongoDB, Bean Validation, MongoDB 8, React 19, React Router, Tailwind CSS 4, Vite 7, Docker Compose, Brevo-planned email delivery with local activation-link logging (001-landing-auth-cellars)

## Project Structure

```text
backend/
frontend/
specs/
```

## Commands

mvn test
npm test && npm run lint
docker compose config

## Code Style

Java for backend services; TypeScript for frontend application: Follow standard conventions

## Recent Changes
- 001-landing-auth-cellars: Added a Docker Compose full-stack plan with Spring Boot 4 + MongoDB 8 backend, React 19 + Vite 7 + Tailwind CSS 4 frontend, Brevo-targeted email delivery via a future provider adapter with local activation-link logging in early iterations, and mandatory unit tests for business functionality in every implementation phase

<!-- MANUAL ADDITIONS START -->
<!-- MANUAL ADDITIONS END -->
