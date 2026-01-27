# Architecture

## Overview
This project is split into a Spring Boot backend and a React frontend backed by PostgreSQL.

```
React (Vite)
   |
   |  /api/*
   v
Spring Boot API
   |
   |  JPA (Hibernate)
   v
PostgreSQL 18.1
```

## Backend
- Java 17, Spring Boot 3.
- HAPI FHIR provides FHIR resource types and server infrastructure.
- Flyway manages database migrations on startup.
- REST endpoints under `/api` support the React frontend, including:
  - `/api/catalog-services`
  - `/api/code-systems`
  - `/api/client-logs`

## Frontend
- React (latest stable) with Vite dev server.
- Proxies `/api` to the backend in local development.
- Catalog Services and Code System Data Entry views are implemented as React components.

## Database
- Schema and test data are managed by Flyway migrations derived from `database/dcr-001`.
