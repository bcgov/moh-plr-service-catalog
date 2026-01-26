# Project Overview
- Spring Boot backend lives in `backend/` and runs as a standalone Java process (no external Tomcat).
- React frontend targets the latest stable React version.
- Database is PostgreSQL.
- Authentication is not in scope for now.

# Layout
- Backend: `backend/`
- Database scripts: `database/`

# Local database setup
- Flyway migrations run from `backend/src/main/resources/db/migration` on startup.
- The destructive database creation script lives in `backend/src/main/resources/db/migration-dev` and is not run by default.
- To include it intentionally (dev only), set `FLYWAY_LOCATIONS=classpath:db/migration,classpath:db/migration-dev` before starting the backend.
- Superuser setup script (run once): `backend/src/main/resources/db/setup/00_create_database.sql`.
