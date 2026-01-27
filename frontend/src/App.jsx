import { useEffect, useMemo, useState } from 'react'
import {
  createCodeSystem,
  createService,
  deleteCodeSystem,
  deleteService,
  listCodeSystems,
  listServices,
  listSystems,
  logClientEvent,
  updateCodeSystem,
} from './api/serviceCatalog'
import './App.css'

function NavItem({ label, isActive, onClick }) {
  return (
    <button
      className={`nav-item ${isActive ? 'active' : ''}`}
      type="button"
      onClick={onClick}
    >
      {label}
    </button>
  )
}

function CatalogServicesView() {
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
    void logClientEvent('debug', 'Catalog Services error', {
      message,
    })
    if (err instanceof TypeError || message.toLowerCase().includes('failed to fetch')) {
      setApiUnavailable(true)
    }
  }

  async function loadServices(reason = 'initial load') {
    setLoading(true)
    setError('')
    void logClientEvent('debug', 'Catalog Services load requested', { reason })
    try {
      const data = await listServices()
      setServices(Array.isArray(data) ? data : [])
      setApiUnavailable(false)
      void logClientEvent('debug', 'Catalog Services load success', {
        count: Array.isArray(data) ? data.length : 0,
      })
    } catch (err) {
      handleError(err, 'Failed to load services.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void logClientEvent('debug', 'Catalog Services view opened')
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
    void logClientEvent('debug', 'Catalog Services submit', {
      name: form.name.trim(),
      systemCode: form.systemCode.trim(),
      hasExternalIdentifier: Boolean(form.externalIdentifier.trim()),
      hasParentLogicalId: Boolean(form.parentLogicalId.trim()),
    })
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
      await loadServices('create')
    } catch (err) {
      handleError(err, 'Failed to create service.')
    } finally {
      setLoading(false)
    }
  }

  async function handleDelete(logicalId) {
    setLoading(true)
    setError('')
    void logClientEvent('debug', 'Catalog Services delete requested', { logicalId })
    try {
      await deleteService(logicalId)
      await loadServices('delete')
    } catch (err) {
      handleError(err, 'Failed to remove service.')
    } finally {
      setLoading(false)
    }
  }

  if (apiUnavailable) {
    return (
      <section className="panel error-card">
        <p className="eyebrow">Backend offline</p>
        <h2>Cannot reach backend APIs</h2>
        <p className="subtitle">
          The frontend is running but could not connect to the backend API. Start the backend or
          verify the API base URL, then retry.
        </p>
        <button className="primary-button" type="button" onClick={() => loadServices('retry')}>
          Retry connection
        </button>
      </section>
    )
  }

  return (
    <div className="view">
      <header className="app-header">
        <div>
          <p className="eyebrow">PLR Service Catalogue</p>
          <h1>Catalog services</h1>
          <p className="subtitle">
            Create and retire catalog services through the backend API.
          </p>
        </div>
        <button className="ghost-button" type="button" onClick={() => loadServices('manual refresh')}>
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
              <span data-label="Logical ID">{service.logicalId ?? '--'}</span>
              <span data-label="Name">{service.name ?? '--'}</span>
              <span data-label="System">{service.systemCode ?? service.system?.code ?? '--'}</span>
              <span data-label="External ID">{service.externalIdentifier ?? '--'}</span>
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

function CodeSystemDataEntryView() {
  const [codeSystems, setCodeSystems] = useState([])
  const [form, setForm] = useState({
    description: '',
    systemUrl: '',
    startDate: '',
    endDate: '',
  })
  const [editingId, setEditingId] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const canSubmit = useMemo(
    () => form.description.trim().length > 0 && form.startDate.trim().length > 0,
    [form]
  )

  function todayIso() {
    return new Date().toISOString().slice(0, 10)
  }

  function resetForm() {
    setForm({
      description: '',
      systemUrl: '',
      startDate: todayIso(),
      endDate: '',
    })
    setEditingId(null)
  }

  function handleChange(event) {
    const { name, value } = event.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  async function loadCodeSystems() {
    setLoading(true)
    setError('')
    try {
      const data = await listCodeSystems()
      setCodeSystems(Array.isArray(data) ? data : [])
    } catch (err) {
      setError(err?.message || 'Failed to load code systems.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    resetForm()
    void loadCodeSystems()
  }, [])

  async function handleSubmit(event) {
    event.preventDefault()
    if (!canSubmit) {
      return
    }

    setLoading(true)
    setError('')
    try {
      const payload = {
        description: form.description.trim(),
        systemUrl: form.systemUrl.trim() || null,
        startDate: form.startDate || null,
        endDate: form.endDate || null,
      }

      if (editingId) {
        await updateCodeSystem(editingId, payload)
      } else {
        await createCodeSystem(payload)
      }
      resetForm()
      await loadCodeSystems()
    } catch (err) {
      setError(err?.message || 'Failed to save code system.')
    } finally {
      setLoading(false)
    }
  }

  function handleEdit(codeSystem) {
    setEditingId(codeSystem.id)
    setForm({
      description: codeSystem.description ?? '',
      systemUrl: codeSystem.systemUrl ?? '',
      startDate: codeSystem.startDate ?? todayIso(),
      endDate: codeSystem.endDate ?? '',
    })
  }

  async function handleDelete(codeSystemId) {
    setLoading(true)
    setError('')
    try {
      await deleteCodeSystem(codeSystemId)
      await loadCodeSystems()
    } catch (err) {
      setError(err?.message || 'Failed to remove code system.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="view">
      <header className="app-header">
        <div>
          <p className="eyebrow">PLR Service Catalogue</p>
          <h1>Code system data entry</h1>
          <p className="subtitle">
            Maintain the code systems used for specialty and service type lookups.
          </p>
        </div>
        <button className="ghost-button" type="button" onClick={loadCodeSystems}>
          Refresh
        </button>
      </header>

      <section className="panel">
        <h2>{editingId ? 'Edit code system' : 'Add a code system'}</h2>
        <form className="form-grid" onSubmit={handleSubmit}>
          <label>
            Description
            <input
              name="description"
              value={form.description}
              onChange={handleChange}
              placeholder="SNOMED CT"
            />
          </label>
          <label>
            Lookup URL
            <input
              name="systemUrl"
              value={form.systemUrl}
              onChange={handleChange}
              placeholder="https://example.org"
            />
          </label>
          <label>
            Start date
            <input
              type="date"
              name="startDate"
              value={form.startDate}
              onChange={handleChange}
            />
          </label>
          <label>
            End date
            <input
              type="date"
              name="endDate"
              value={form.endDate}
              onChange={handleChange}
            />
          </label>
          <div className="form-actions">
            <button className="primary-button" type="submit" disabled={!canSubmit || loading}>
              {editingId ? 'Save changes' : 'Add code system'}
            </button>
            {editingId && (
              <button className="ghost-button" type="button" onClick={resetForm}>
                Cancel edit
              </button>
            )}
          </div>
        </form>
        {error && <p className="error">{error}</p>}
      </section>

      <section className="panel">
        <div className="panel-header">
          <h2>Code systems</h2>
          {loading && <span className="status">Updating</span>}
        </div>
        <div className="table">
          <div className="table-row table-head code-system-row">
            <span>ID</span>
            <span>Description</span>
            <span>Lookup URL</span>
            <span>Start</span>
            <span>End</span>
            <span>Actions</span>
          </div>
          {codeSystems.length === 0 && !loading && (
            <div className="table-row empty">
              <span>No code systems returned yet.</span>
            </div>
          )}
          {codeSystems.map((system) => (
            <div className="table-row code-system-row" key={system.id ?? system.codeSystemId}>
              <span data-label="ID">{system.id ?? system.codeSystemId ?? '--'}</span>
              <span data-label="Description">{system.description ?? '--'}</span>
              <span data-label="Lookup URL">{system.systemUrl ?? '--'}</span>
              <span data-label="Start">{system.startDate ?? '--'}</span>
              <span data-label="End">{system.endDate ?? '--'}</span>
              <span className="action-group">
                <button
                  className="ghost-button"
                  type="button"
                  onClick={() => handleEdit(system)}
                >
                  Edit
                </button>
                <button
                  className="ghost-button"
                  type="button"
                  onClick={() => handleDelete(system.id ?? system.codeSystemId)}
                  disabled={loading}
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

function TestView() {
  return (
    <div className="view">
      <section className="panel">
        <p className="eyebrow">Coming soon</p>
        <h2>Test</h2>
        <p className="subtitle">
          Placeholder view for the Test section.
        </p>
      </section>
    </div>
  )
}

function App() {
  const [activeView, setActiveView] = useState('catalog')
  const navItems = [
    { id: 'catalog', label: 'Catalog Services' },
    { id: 'code-system', label: 'Code System Data Entry' },
    { id: 'test', label: 'Test' },
  ]

  return (
    <div className="app-shell">
      <header className="top-bar">
        <div className="brand">
          <p className="eyebrow">PLR Service Catalogue</p>
          <span className="brand-title">Service Catalog</span>
        </div>
        <nav className="nav">
          {navItems.map((item) => (
            <NavItem
              key={item.id}
              label={item.label}
              isActive={activeView === item.id}
              onClick={() => {
                setActiveView(item.id)
                void logClientEvent('debug', 'Navigation changed', { view: item.id })
              }}
            />
          ))}
        </nav>
      </header>

      <main className="app-content">
        {activeView === 'catalog' && <CatalogServicesView />}
        {activeView === 'code-system' && <CodeSystemDataEntryView />}
        {activeView === 'test' && <TestView />}
      </main>
    </div>
  )
}

export default App
