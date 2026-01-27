# Architectural Diagram

```mermaid
graph LR
  User([Business user])
  Dev([Developer])
  FE[React frontend<br/>(Vite dev server)]
  BE[Spring Boot backend<br/>Java 17]
  FHIR[HAPI FHIR servlet<br/>/ServiceCatalogue/*]
  REST[REST API<br/>/api/catalog-services<br/>/api/code-systems<br/>/api/client-logs]
  DB[(PostgreSQL 18.1)]

  User --> FE
  Dev --> FE
  Dev --> BE
  FE --> REST
  BE --> REST
  BE --> FHIR
  BE --> DB
```
