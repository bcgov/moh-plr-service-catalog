import { useEffect, useMemo, useState } from 'react'
import { createService, deleteService, listServices, listSystems } from './api/serviceCatalog'
import './App.css'

function App() {
  const [services, setServices] = useState([])
  const [form, setForm] = useState({
    name: '',
    description: '',
    externalIdentifier: '',
    systemCode: '',
    parentLogicalId: '',
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [apiUnavailable, setApiUnavailable] = useState(false)
  const [systems, setSystems] = useState([])
  const canSubmit = useMemo(
    () =>
      form.name.trim().length > 0 &&
      form.description.trim().length > 0 &&
      form.systemCode.trim().length > 0,
    [form]
  )

  function friendlyErrorMessage(message) {
    const normalized = message.toLowerCase()
    if (normalized.includes('500')) {
      return 'The backend API or database is unavailable. Please try again shortly.'
    }
    if (normalized.includes('failed to fetch') || normalized.includes('network')) {
      return 'Cannot reach the backend API. Please verify it is running.'
    }
    return message
  }

  function handleError(err, fallback) {
    const message = err?.message || fallback
    const friendly = friendlyErrorMessage(message)
    setError(friendly)
    if (err instanceof TypeError || message.toLowerCase().includes('failed to fetch')) {
      setApiUnavailable(true)
    }
  }

  async function loadServices() {
    setLoading(true)
    setError('')
    try {
      const data = await listServices()
      setServices(Array.isArray(data) ? data : [])
      setApiUnavailable(false)
    } catch (err) {
      handleError(err, 'Failed to load services.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadServices()
    void loadSystems()
  }, [])

  async function loadSystems() {
    try {
      const data = await listSystems()
      setSystems(Array.isArray(data) ? data : [])
    } catch (err) {
      handleError(err, 'Failed to load system codes.')
    }
  }

  function handleChange(event) {
    const { name, value } = event.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    if (!canSubmit) {
      return
    }

    setLoading(true)
    setError('')
    try {
      await createService({
        name: form.name.trim(),
        description: form.description.trim(),
        externalIdentifier: form.externalIdentifier.trim() || null,
        systemCode: form.systemCode.trim(),
        parentLogicalId: form.parentLogicalId.trim() || null,
      })
      setForm({
        name: '',
        description: '',
        externalIdentifier: '',
        systemCode: '',
        parentLogicalId: '',
      })
      await loadServices()
    } catch (err) {
      handleError(err, 'Failed to create service.')
    } finally {
      setLoading(false)
    }
  }

  async function handleDelete(logicalId) {
    setLoading(true)
    setError('')
    try {
      await deleteService(logicalId)
      await loadServices()
    } catch (err) {
      handleError(err, 'Failed to remove service.')
    } finally {
      setLoading(false)
    }
  }

  if (apiUnavailable) {
    return (
      <div className="app error-page">
        <div className="error-card">
          <p className="eyebrow">Backend offline</p>
          <h1>Cannot reach backend APIs</h1>
          <p className="subtitle">
            The frontend is running but could not connect to the backend API. Start the backend or
            verify the API base URL, then retry.
          </p>
          <button className="primary-button" type="button" onClick={loadServices}>
            Retry connection
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="app">
      <header className="app-header">
        <div>
          <p className="eyebrow">PLR Service Catalogue</p>
          <h1>Catalog services</h1>
          <p className="subtitle">
            Create and retire catalog services through the backend API.
          </p>
        </div>
        <button className="ghost-button" type="button" onClick={loadServices}>
          Refresh
        </button>
      </header>

      <section className="panel">
        <h2>Add a service</h2>
        <form className="form-grid" onSubmit={handleSubmit}>
          <label>
            Name
            <input
              name="name"
              value={form.name}
              onChange={handleChange}
              placeholder="MRI"
            />
          </label>
          <label>
            Description
            <input
              name="description"
              value={form.description}
              onChange={handleChange}
              placeholder="Magnetic Resonance Imaging"
            />
          </label>
          <label>
            External identifier
            <input
              name="externalIdentifier"
              value={form.externalIdentifier}
              onChange={handleChange}
              placeholder="MRI"
            />
          </label>
          <label>
            System code
            <select
              name="systemCode"
              value={form.systemCode}
              onChange={handleChange}
            >
              <option value="">Select a system</option>
              {systems.map((system) => (
                <option key={system.id ?? system.code} value={system.code}>
                  {system.code} {system.description ? `- ${system.description}` : ''}
                </option>
              ))}
            </select>
          </label>
          <label>
            Parent logical ID
            <input
              name="parentLogicalId"
              value={form.parentLogicalId}
              onChange={handleChange}
              placeholder="Optional"
            />
          </label>
          <button className="primary-button" type="submit" disabled={!canSubmit || loading}>
            Add service
          </button>
        </form>
        {error && <p className="error">{error}</p>}
      </section>

      <section className="panel">
        <div className="panel-header">
          <h2>Services</h2>
          {loading && <span className="status">Updating</span>}
        </div>
        <div className="table">
          <div className="table-row table-head">
            <span>Logical ID</span>
            <span>Name</span>
            <span>System</span>
            <span>External ID</span>
            <span>Actions</span>
          </div>
          {services.length === 0 && !loading && (
            <div className="table-row empty">
              <span>No services returned yet.</span>
            </div>
          )}
          {services.map((service) => (
            <div className="table-row" key={service.logicalId ?? service.id ?? service.serviceId}>
              <span>{service.logicalId ?? '—'}</span>
              <span>{service.name ?? '—'}</span>
              <span>{service.systemCode ?? service.system?.code ?? '—'}</span>
              <span>{service.externalIdentifier ?? '—'}</span>
              <span>
                <button
                  className="ghost-button"
                  type="button"
                  onClick={() => handleDelete(service.logicalId)}
                  disabled={!service.logicalId || loading}
                >
                  Remove
                </button>
              </span>
            </div>
          ))}
        </div>
      </section>
    </div>
  )
}

export default App
