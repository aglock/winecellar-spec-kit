import React, { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { NavBar, Header, Card, Button, AsyncStateMessage } from '../components/index'
import { listDefaultCellarBottles } from '../services/bottleService'

function CellarView({ onSignOut, refreshToken = 0, initialBottles = null }) {
  const { cellarId } = useParams()
  const [bottles, setBottles] = useState(initialBottles || [])
  const [isLoading, setIsLoading] = useState(!initialBottles)
  const [loadError, setLoadError] = useState('')
  const [page, setPage] = useState(0)
  const [size] = useState(50)
  const [total, setTotal] = useState(initialBottles ? initialBottles.length : 0)
  const [user] = useState({ username: 'Collector' })
  const [selectedBottleId, setSelectedBottleId] = useState(null)

  useEffect(() => {
    if (initialBottles !== null) {
      return undefined
    }

    let cancelled = false

    const loadBottles = async () => {
      setIsLoading(true)
      setLoadError('')
      try {
        const response = await listDefaultCellarBottles({ page, size })
        if (cancelled) {
          return
        }
        setBottles(response.items || [])
        setTotal(response.total || 0)
      } catch (error) {
        if (!cancelled) {
          setLoadError(error.message || 'Failed to load bottles')
        }
      } finally {
        if (!cancelled) {
          setIsLoading(false)
        }
      }
    }

    loadBottles()

    return () => {
      cancelled = true
    }
  }, [initialBottles, page, refreshToken, size])

  if (cellarId && cellarId !== 'default') {
    return (
      <div className="min-h-screen bg-canvas flex items-center justify-center">
        <div className="text-center">
          <h1 className="heading-lg text-text-primary mb-4">Cellar Not Found</h1>
          <Link to="/cellars">
            <Button variant="primary">Back to Cellars</Button>
          </Link>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-canvas">
      <NavBar user={user} onSignOut={onSignOut} />

      <div className="max-w-6xl mx-auto px-8 py-12">
        <div className="bg-surface-raised rounded-lg border border-border-subtle p-8 mb-8">
          <div className="flex justify-between items-start mb-4">
            <div>
              <Link to="/cellars" className="body-sm text-brand-primary hover:text-brand-primary-hover mb-2 block">
                ← Back to Cellars
              </Link>
              <h1 className="display-lg text-brand-primary">Default Cellar</h1>
              <p className="body-lg text-text-secondary mt-2">Imported bottle inventory</p>
            </div>
            <div className="text-right">
              <div className="display-md text-brand-secondary">{total}</div>
              <p className="body-sm text-text-muted">bottles</p>
            </div>
          </div>
          <p className="body-md text-text-secondary">
            Browse and verify bottles imported from your CSV source.
          </p>
        </div>

        <Header 
          title="Inventory"
          subtitle="All bottles currently mapped to the default cellar"
        />

        {isLoading && (
          <Card className="mb-4">
            <AsyncStateMessage
              type="info"
              title="Loading inventory"
              message="Fetching your latest bottle list from the cellar service."
            />
          </Card>
        )}

        {loadError && (
          <Card className="mb-4 border-danger">
            <AsyncStateMessage
              type="error"
              title="Could not load bottles"
              message={loadError}
            />
          </Card>
        )}

        {!isLoading && !loadError && bottles.length > 0 ? (
          <div className="grid grid-cols-1 gap-4">
            {bottles.map((bottle) => (
              <Card 
                key={bottle.bottleId}
                className="cursor-pointer hover:shadow-md transition-shadow hover:border-brand-primary"
                onClick={() => setSelectedBottleId((current) => current === bottle.bottleId ? null : bottle.bottleId)}
              >
                <div className="grid grid-cols-1 md:grid-cols-12 gap-6 items-start">
                  <div className="md:col-span-7">
                    <h3 className="heading-sm text-text-primary mb-2">{bottle.wineName}</h3>
                    <div className="flex flex-wrap gap-3 mb-4">
                      <span className="label text-brand-secondary bg-brand-secondary-soft px-3 py-1 rounded-full">
                        {bottle.vintageDisplay}
                      </span>
                      <span className="label text-accent-olive bg-accent-olive-soft px-3 py-1 rounded-full">
                        {bottle.wineType}
                      </span>
                      <span className="label text-text-muted bg-surface-tinted px-3 py-1 rounded-full">
                        {bottle.region}
                      </span>
                    </div>
                    <p className="body-sm text-text-secondary mb-2">
                      <strong>Producer:</strong> {bottle.producerName}
                    </p>
                    <p className="body-sm text-text-muted">
                      {bottle.country} • {bottle.bottleSize}
                    </p>
                  </div>

                  <div className="md:col-span-5 space-y-4 md:border-l md:border-border-subtle md:pl-6">
                    <div>
                      <p className="label text-text-muted mb-1">Quantity</p>
                      <p className="heading-sm text-text-primary">{bottle.quantity}</p>
                    </div>
                  </div>
                </div>

                {selectedBottleId === bottle.bottleId && (
                  <div className="mt-6 pt-6 border-t border-border-subtle bg-surface-tinted p-4 rounded-md">
                    <h4 className="title text-text-primary mb-3">Additional Information</h4>
                    <div className="grid grid-cols-2 md:grid-cols-3 gap-4 text-body-sm">
                      <div>
                        <p className="label text-text-muted">Bottle ID</p>
                        <p className="text-text-primary break-all">{bottle.bottleId}</p>
                      </div>
                      <div>
                        <p className="label text-text-muted">Country</p>
                        <p className="text-text-primary">{bottle.country}</p>
                      </div>
                      <div>
                        <p className="label text-text-muted">Size</p>
                        <p className="text-text-primary">{bottle.bottleSize}</p>
                      </div>
                    </div>
                  </div>
                )}
              </Card>
            ))}
          </div>
        ) : !isLoading && !loadError ? (
          <Card className="text-center py-12 bg-brand-primary-soft border-brand-primary">
            <div className="text-6xl mb-4">🍾</div>
            <AsyncStateMessage
              type="warning"
              title="No bottles yet"
              message="Import a CSV file from the cellar overview to populate this list."
            />
          </Card>
        ) : null}

        {!isLoading && !loadError && (
          <div className="flex items-center justify-between mt-6">
            <Button
              variant="secondary"
              size="sm"
              disabled={page === 0}
              onClick={() => setPage((current) => Math.max(0, current - 1))}
            >
              Previous
            </Button>
            <p className="body-sm text-text-secondary">Page {page + 1}</p>
            <Button
              variant="secondary"
              size="sm"
              disabled={(page + 1) * size >= total}
              onClick={() => setPage((current) => current + 1)}
            >
              Next
            </Button>
          </div>
        )}
      </div>
    </div>
  )
}

export default CellarView
