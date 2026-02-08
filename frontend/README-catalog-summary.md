# Service Catalog Summary Feature

## Technical Specifications: Service Summary Display

### 1. Backend API Endpoint
- **Endpoint:** `/api/catalog-services/service-counts`
- **Purpose:** Returns a JSON array with the count of services grouped by system code.
- **Response Example:**
  ```json
  [
    { "systemCode": "SYS1", "count": 5 },
    { "systemCode": "SYS2", "count": 3 }
  ]
  ```

### 2. Frontend API Integration
- **File:** `frontend/src/api/serviceCatalog.js`
- **Function:** `getServiceCountsBySystemCode`
- **Implementation:**  
  - Uses a relative URL (`/api/catalog-services/service-counts`) to leverage Vite’s dev server proxy for local development.
  - Returns the parsed JSON response from the backend.

### 3. Frontend UI Component
- **File:** `frontend/src/App.jsx`
- **Component:** `CatalogSummaryView`
- **Logic:**
  - On mount (`useEffect`), calls `getServiceCountsBySystemCode`.
  - Stores the result in the `serviceSummary` state.
  - Displays a table with system code and service count.
  - Handles loading and error states.

### 4. Import Correction
- **File:** `frontend/src/App.jsx`
- **Change:**  
  - Added `getServiceCountsBySystemCode` to the import list from `./api/serviceCatalog` to ensure the function is available in the component.

### 5. Proxy Configuration
- **File:** `frontend/vite.config.js`
- **Proxy:**  
  - All `/api` requests are proxied to `http://localhost:8080` during development, allowing the frontend to communicate with the backend without CORS issues.

### 6. Environment Configuration (Optional for Production)
- **File:** `frontend/.env`
- **Variable:**  
  - `VITE_API_BASE_URL=http://localhost:8080` (recommended for production or when not using the Vite proxy).

---

**Summary:**  
These changes ensure that the Catalog Summary page fetches and displays service counts from the backend, using a consistent API pattern and working seamlessly in both development and production environments.
