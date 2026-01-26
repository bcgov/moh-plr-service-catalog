# Service Catalog Frontend

React frontend for managing the PLR Service Catalogue through backend APIs.

## Development
1. `npm install`
2. `npm run dev`

The dev server proxies `/api` to `http://localhost:8080` by default.

## API expectations
The UI assumes the backend exposes JSON endpoints:
- `GET /api/catalog-services` returns a list of services.
- `POST /api/catalog-services` accepts `{ name, description, externalIdentifier, systemCode, parentLogicalId }`.
- `DELETE /api/catalog-services/{logicalId}` removes a service.

To change the API base, set `VITE_API_BASE_URL` in a `.env` file, for example:
```
VITE_API_BASE_URL=http://localhost:8080
```
