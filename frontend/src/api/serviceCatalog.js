export function getServiceCountsBySystemCode() {
  // Use a relative URL so the Vite dev server proxy works in dev, just like other APIs
  return request('/api/catalog-services/service-counts')
}
const API_BASE = import.meta.env.VITE_API_BASE_URL || ''

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    },
    ...options,
  })

  if (!response.ok) {
    const message = await response.text()
    throw new Error(message || `Request failed with ${response.status}`)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

export function listServices() {
  return request('/api/catalog-services')
}

export function createService(payload) {
  return request('/api/catalog-services', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function deleteService(logicalId) {
  return request(`/api/catalog-services/${encodeURIComponent(logicalId)}`, {
    method: 'DELETE',
  })
}

export function listSystems() {
  return request('/api/catalog-services/systems')
}

export function listCodeSystems() {
  return request('/api/code-systems')
}

export function createCodeSystem(payload) {
  return request('/api/code-systems', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function updateCodeSystem(codeSystemId, payload) {
  return request(`/api/code-systems/${encodeURIComponent(codeSystemId)}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

export function deleteCodeSystem(codeSystemId) {
  return request(`/api/code-systems/${encodeURIComponent(codeSystemId)}`, {
    method: 'DELETE',
  })
}

export function logClientEvent(level, message, context) {
  const payload = {
    message,
    level,
    context: context ? JSON.stringify(context) : null,
  }
  return request('/api/client-logs', {
    method: 'POST',
    body: JSON.stringify(payload),
  }).catch(() => null)
}
