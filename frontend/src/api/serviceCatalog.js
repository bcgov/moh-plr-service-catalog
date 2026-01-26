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
