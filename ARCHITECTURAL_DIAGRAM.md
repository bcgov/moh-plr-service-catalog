# Architectural Diagram

```mermaid
flowchart LR
  User([Business user])
  Dev([Developer])
  FE[React frontend\n(Vite dev server)]
  BE[Spring Boot backend\nJava 17]
  FHIR[HAPI FHIR servlet\n/ServiceCatalogue/*]
  REST[REST API\n/api/catalog-services]
  DB[(PostgreSQL 18.1)]

  User --> FE
  Dev --> FE
  Dev --> BE
  FE --> REST
  BE --> REST
  BE --> FHIR
  BE --> DB
```
