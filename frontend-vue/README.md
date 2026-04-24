# Winecellar Frontend (Vue)

A separate Vue.js implementation of the CSV import + bottle list workflow defined in [specs/001-csv-bottle-import](../specs/001-csv-bottle-import).

## Purpose

This project mirrors the current React frontend functionality while reusing the same backend APIs.

## Quick Start

```bash
cd frontend-vue
npm install
npm run dev
```

The Vue frontend runs on `http://localhost:5174`.

## Backend API Configuration

Set the backend base URL with a Vite env variable:

```bash
VITE_API_BASE_URL=http://localhost:8080
```

If not provided, the frontend defaults to `http://localhost:8080`.

## Feature Parity Scope

- Landing page and mock sign-in
- Cellar overview with CSV upload form
- Import summary and skipped-row reporting
- Bottle list view for the default cellar with pagination
- Loading, error, and empty states

## Build

```bash
npm run build
```
