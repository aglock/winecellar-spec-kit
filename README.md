# winecellar-spec-kit

Wine cellar application to help users manage shared cellars, activation-based
account access, and authenticated cellar listings.

## Local Development

### Backend

```bash
mvn -f backend/pom.xml test
```

### Frontend

```bash
cd frontend
npm install
npm test
npm run lint
```

### Full Stack

```bash
docker compose config
docker compose up --build
```

The current activation flow logs activation links in the backend application
log for local development. The delivery path is intentionally abstracted so a
Brevo-backed adapter can replace the log-based implementation later.
