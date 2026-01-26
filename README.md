# PLR Service Catalogue

Service Catalogue exposes BC Catalogue HealthcareService resources and supports search by name, system, specialty, type, parent, or external identifier. It is split into a Spring Boot backend and a React frontend backed by PostgreSQL.

## Project layout
- `backend/` Spring Boot API (Java 17, Spring Boot 3).
- `frontend/` React app (latest stable React).
- `database/` Reference SQL scripts.

## Prerequisites
- Java 17+
- Maven
- Node.js 18+
- PostgreSQL 18.1 (local dev)

## Backend (Spring Boot)
Run the API from the repo root:
```bash
mvn -f backend/pom.xml spring-boot:run
```

The backend exposes:
- REST API for the frontend at `/api/catalog-services`
- HAPI FHIR endpoint at `/ServiceCatalogue/*`

The backend uses local dev defaults in `backend/src/main/resources/application.yml`:
- JDBC URL: `jdbc:postgresql://localhost:5432/plr_hs_catalog`
- Username: `plr_user`
- Password: `plr_password`

### Create local database and user (once)
Run these as a PostgreSQL superuser:
```sql
CREATE USER plr_user WITH PASSWORD 'plr_password';

CREATE DATABASE plr_hs_catalog
    WITH OWNER = plr_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

GRANT ALL PRIVILEGES ON DATABASE plr_hs_catalog TO plr_user;
```
The same script is also available at:
```
backend/src/main/resources/db/setup/00_create_database.sql
```

### Database migrations (Flyway)
Migrations run automatically on startup from:
```
backend/src/main/resources/db/migration
```

The destructive database creation script is dev-only and not executed by default:
```
backend/src/main/resources/db/migration-dev/V1__create_database.sql
```

To include it intentionally (dev only), set:
```bash
set FLYWAY_LOCATIONS=classpath:db/migration,classpath:db/migration-dev
```

## Frontend (React)
From the repo root:
```bash
cd frontend
npm install
npm run dev
```

The dev server proxies `/api` to `http://localhost:8080` and expects:
- `GET /api/catalog-services`
- `POST /api/catalog-services`
- `DELETE /api/catalog-services/{logicalId}`

To override the API base URL, create `frontend/.env`:
```
VITE_API_BASE_URL=http://localhost:8080
```

## For business users
Use the React frontend to add and remove catalog services through the backend API.



