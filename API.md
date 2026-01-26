# API

Base URL (local dev): `http://localhost:8080`

## Catalog Services

### List services
```
GET /api/catalog-services
```

Response (example):
```json
[
  {
    "logicalId": 101,
    "name": "MRI",
    "description": "Magnetic Resonance Imaging",
    "externalIdentifier": "MRI",
    "systemCode": "PHSA",
    "parentLogicalId": null
  }
]
```

### Create service
```
POST /api/catalog-services
```

Request body:
```json
{
  "name": "MRI",
  "description": "Magnetic Resonance Imaging",
  "externalIdentifier": "MRI",
  "systemCode": "PHSA",
  "parentLogicalId": null
}
```

Response: `201 Created`

### Delete service
```
DELETE /api/catalog-services/{logicalId}
```

Response: `204 No Content`

## Notes
- These endpoints are expected by the frontend.
- Authentication is not implemented yet.
- The separate HAPI FHIR endpoint is available at `/ServiceCatalogue/*`.
