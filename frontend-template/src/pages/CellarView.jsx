import React, { useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { NavBar, Header, Card, Button } from '../components/index'
import { mockCellars, mockBottles } from '../mockData'

function CellarView({ onSignOut }) {
  const { cellarId } = useParams()
  const [cellar] = useState(() => mockCellars.find(c => c.id === cellarId))
  const [bottles] = useState(() => mockBottles[cellarId] || [])
  const [user] = useState({ username: 'Collector' })
  const [selectedBottle, setSelectedBottle] = useState(null)

  if (!cellar) {
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
        {/* Cellar Header */}
        <div className="bg-surface-raised rounded-lg border border-border-subtle p-8 mb-8">
          <div className="flex justify-between items-start mb-4">
            <div>
              <Link to="/cellars" className="body-sm text-brand-primary hover:text-brand-primary-hover mb-2 block">
                ← Back to Cellars
              </Link>
              <h1 className="display-lg text-brand-primary">{cellar.name}</h1>
              <p className="body-lg text-text-secondary mt-2">{cellar.region}</p>
            </div>
            <div className="text-right">
              <div className="display-md text-brand-secondary">{bottles.length}</div>
              <p className="body-sm text-text-muted">bottles</p>
            </div>
          </div>
          <p className="body-md text-text-secondary">{cellar.description}</p>
        </div>

        <Header 
          title="Inventory"
          subtitle="All bottles in this cellar"
          action={
            <Button variant="primary">+ Add Bottle</Button>
          }
        />

        {/* Bottles Grid */}
        {bottles.length > 0 ? (
          <div className="grid grid-cols-1 gap-4">
            {bottles.map((bottle) => (
              <Card 
                key={bottle.id}
                className="cursor-pointer hover:shadow-md transition-shadow hover:border-brand-primary"
                onClick={() => setSelectedBottle(selectedBottle?.id === bottle.id ? null : bottle)}
              >
                <div className="grid grid-cols-1 md:grid-cols-12 gap-6 items-start">
                  {/* Main Info */}
                  <div className="md:col-span-7">
                    <h3 className="heading-sm text-text-primary mb-2">{bottle.name}</h3>
                    <div className="flex flex-wrap gap-3 mb-4">
                      <span className="label text-brand-secondary bg-brand-secondary-soft px-3 py-1 rounded-full">
                        {bottle.vintage}
                      </span>
                      <span className="label text-accent-olive bg-accent-olive-soft px-3 py-1 rounded-full">
                        {bottle.type}
                      </span>
                      <span className="label text-text-muted bg-surface-tinted px-3 py-1 rounded-full">
                        {bottle.region}
                      </span>
                    </div>
                    <p className="body-sm text-text-secondary mb-2">
                      <strong>Grapes:</strong> {bottle.grapes}
                    </p>
                    <p className="body-sm text-text-muted">
                      {bottle.notes}
                    </p>
                  </div>

                  {/* Side Info */}
                  <div className="md:col-span-5 space-y-4 md:border-l md:border-border-subtle md:pl-6">
                    <div>
                      <p className="label text-text-muted mb-1">Position</p>
                      <p className="heading-sm text-text-primary">{bottle.position}</p>
                    </div>
                    <div>
                      <p className="label text-text-muted mb-1">Alcohol</p>
                      <p className="body-md text-text-primary">{bottle.alcohol}</p>
                    </div>
                    <div className="flex gap-2">
                      <Button variant="secondary" size="sm" className="flex-1">
                        Edit
                      </Button>
                      <Button variant="danger" size="sm" className="flex-1">
                        Remove
                      </Button>
                    </div>
                  </div>
                </div>

                {/* Expanded Details */}
                {selectedBottle?.id === bottle.id && (
                  <div className="mt-6 pt-6 border-t border-border-subtle bg-surface-tinted p-4 rounded-md">
                    <h4 className="title text-text-primary mb-3">Additional Information</h4>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-body-sm">
                      <div>
                        <p className="label text-text-muted">Status</p>
                        <p className="text-text-primary">Available</p>
                      </div>
                      <div>
                        <p className="label text-text-muted">Added</p>
                        <p className="text-text-primary">2024-03-15</p>
                      </div>
                      <div>
                        <p className="label text-text-muted">Last Tasted</p>
                        <p className="text-text-primary">Never</p>
                      </div>
                      <div>
                        <p className="label text-text-muted">Rating</p>
                        <p className="text-text-primary">—</p>
                      </div>
                    </div>
                  </div>
                )}
              </Card>
            ))}
          </div>
        ) : (
          <Card className="text-center py-12 bg-brand-primary-soft border-brand-primary">
            <div className="text-6xl mb-4">🍾</div>
            <h3 className="heading-md text-text-primary mb-2">No bottles yet</h3>
            <p className="body-md text-text-secondary">
              Start adding bottles to this cellar
            </p>
            <Button variant="primary" size="lg" className="mt-6">
              Add First Bottle
            </Button>
          </Card>
        )}
      </div>
    </div>
  )
}

export default CellarView
