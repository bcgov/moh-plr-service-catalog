# Development

## Prerequisites
- Java 17+
- Maven
- Node.js 18+
- PostgreSQL 18.1 (local dev)

## Backend
From the repo root:
```bash
mvn -f backend/pom.xml spring-boot:run
```

### Database configuration (local dev)
`backend/src/main/resources/application.yml` defaults to:
- `jdbc:postgresql://localhost:5432/plr_hs_catalog`
- `plr_user / plr_password`

Superuser setup script (run once):
```
backend/src/main/resources/db/setup/00_create_database.sql
```

### Flyway migrations
Run automatically on startup from:
```
backend/src/main/resources/db/migration
```

Dev-only database creation (destructive) lives here and is not run by default:
```
backend/src/main/resources/db/migration-dev/V1__create_database.sql
```

To include it intentionally (dev only), set:
```bash
set FLYWAY_LOCATIONS=classpath:db/migration,classpath:db/migration-dev
```

## Frontend
From the repo root:
```bash
cd frontend
npm install
npm run dev
```

### API base URL
The Vite dev server proxies `/api` to `http://localhost:8080`.

To override, create `frontend/.env`:
```
VITE_API_BASE_URL=http://localhost:8080
```

## Troubleshooting
- Backend port already in use: stop the existing process or change `server.port` in `backend/src/main/resources/application.yml`.
- API errors in frontend: confirm backend is running and reachable at `http://localhost:8080`.
