import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import { NavBar, Header, Card, Button } from '../components/index'
import { mockCellars } from '../mockData'
import { importWineBottlesCsv } from '../services/importService'

function CellarOverview({ user, onSignOut, onImportCompleted }) {
  const [cellars] = useState(mockCellars)
  const [showNewCellar, setShowNewCellar] = useState(false)
  const [selectedFile, setSelectedFile] = useState(null)
  const [uploadError, setUploadError] = useState('')
  const [isUploading, setIsUploading] = useState(false)
  const [importSummary, setImportSummary] = useState(null)

  const handleUploadSubmit = async (event) => {
    event.preventDefault()
    setUploadError('')

    if (!selectedFile) {
      setUploadError('Select a CSV file before uploading.')
      return
    }

    setIsUploading(true)
    try {
      const summary = await importWineBottlesCsv(selectedFile)
      setImportSummary(summary)
      if (onImportCompleted) {
        onImportCompleted()
      }
      setSelectedFile(null)
      event.target.reset()
    } catch (error) {
      setUploadError(error.message || 'Upload failed. Please try again.')
    } finally {
      setIsUploading(false)
    }
  }

  return (
    <div className="min-h-screen bg-canvas">
      <NavBar user={user} onSignOut={onSignOut} />

      <div className="max-w-6xl mx-auto px-8 py-12">
        <Header 
          title="My Cellars"
          subtitle="Select a cellar to view and manage your bottles"
          action={
            <Button 
              variant="primary"
              onClick={() => setShowNewCellar(!showNewCellar)}
            >
              + New Cellar
            </Button>
          }
        />

        <Card className="mb-8 bg-brand-primary-soft border-brand-primary">
          <h3 className="heading-sm text-text-primary mb-2">Import wine bottles (CSV)</h3>
          <p className="body-sm text-text-secondary mb-4">
            Upload your cellar CSV file to add bottles and receive an import summary.
          </p>

          <form onSubmit={handleUploadSubmit} className="space-y-4">
            <div>
              <label className="label text-text-primary block mb-2" htmlFor="csv-upload-input">
                CSV file
              </label>
              <input
                id="csv-upload-input"
                type="file"
                accept=".csv,text/csv"
                onChange={(event) => {
                  const nextFile = event.target.files && event.target.files.length > 0
                    ? event.target.files[0]
                    : null
                  setSelectedFile(nextFile)
                  setUploadError('')
                }}
                className="w-full px-4 py-3 rounded-sm border border-border-subtle bg-surface-raised text-text-primary focus:outline-none focus:ring-2 focus:ring-focus"
              />
            </div>

            <div className="flex items-center gap-3">
              <Button variant="primary" size="sm" type="submit" disabled={isUploading}>
                {isUploading ? 'Uploading...' : 'Upload CSV'}
              </Button>
              {selectedFile && (
                <span className="body-sm text-text-secondary">{selectedFile.name}</span>
              )}
            </div>
          </form>

          {uploadError && (
            <p className="mt-4 body-sm text-danger">{uploadError}</p>
          )}

          {importSummary && (
            <div className="mt-5 p-4 rounded-sm border border-border-subtle bg-surface-raised">
              <h4 className="heading-sm text-text-primary mb-3">Latest import summary</h4>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mb-4">
                <div>
                  <p className="label text-text-muted">Imported</p>
                  <p className="heading-sm text-text-primary">{importSummary.importedCount}</p>
                </div>
                <div>
                  <p className="label text-text-muted">Skipped</p>
                  <p className="heading-sm text-text-primary">{importSummary.skippedCount}</p>
                </div>
                <div>
                  <p className="label text-text-muted">Event ID</p>
                  <p className="body-sm text-text-secondary break-all">{importSummary.eventId}</p>
                </div>
              </div>

              {importSummary.skippedRows && importSummary.skippedRows.length > 0 && (
                <div>
                  <h5 className="label text-text-primary mb-2">Skipped rows</h5>
                  <div className="space-y-2 max-h-48 overflow-auto pr-2">
                    {importSummary.skippedRows.map((row) => (
                      <div key={`${row.rowNumber}-${row.reason}`} className="text-body-sm p-3 rounded-sm border border-border-subtle bg-canvas">
                        <span className="font-semibold text-text-primary">Row {row.rowNumber}:</span>{' '}
                        <span className="text-text-secondary">{row.reason}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}
        </Card>

        {showNewCellar && (
          <Card className="mb-8 bg-brand-primary-soft border-brand-primary">
            <h3 className="heading-sm text-text-primary mb-4">Create a New Cellar</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
              <input
                type="text"
                placeholder="Cellar name"
                className="px-4 py-3 rounded-sm border border-border-subtle bg-surface-raised text-text-primary focus:outline-none focus:ring-2 focus:ring-focus"
              />
              <input
                type="text"
                placeholder="Region or description"
                className="px-4 py-3 rounded-sm border border-border-subtle bg-surface-raised text-text-primary focus:outline-none focus:ring-2 focus:ring-focus"
              />
            </div>
            <div className="flex gap-3">
              <Button variant="primary" size="sm">Create</Button>
              <Button variant="secondary" size="sm" onClick={() => setShowNewCellar(false)}>
                Cancel
              </Button>
            </div>
          </Card>
        )}

        {/* Cellars Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {cellars.map((cellar) => (
            <Link key={cellar.id} to={`/cellars/${cellar.id}`}>
              <Card className="h-full cursor-pointer hover:shadow-md transition-shadow hover:border-brand-primary">
                <div className="flex justify-between items-start mb-4">
                  <h2 className="heading-sm text-text-primary">{cellar.name}</h2>
                  <span className="label text-brand-secondary bg-brand-secondary-soft px-3 py-1 rounded-full">
                    {cellar.bottleCount}
                  </span>
                </div>
                
                <p className="body-sm text-text-secondary mb-2">{cellar.region}</p>
                <p className="body-sm text-text-muted mb-4">{cellar.description}</p>
                
                <div className="flex justify-between items-center pt-4 border-t border-border-subtle">
                  <span className="label text-text-muted">Created {cellar.createdAt}</span>
                  <span className="text-brand-primary">→</span>
                </div>
              </Card>
            </Link>
          ))}
        </div>

        {cellars.length === 0 && (
          <Card className="text-center py-12 bg-brand-primary-soft border-brand-primary">
            <div className="text-6xl mb-4">🍾</div>
            <h3 className="heading-md text-text-primary mb-2">No cellars yet</h3>
            <p className="body-md text-text-secondary">
              Create your first cellar to start building your collection
            </p>
            <Button variant="primary" size="lg" className="mt-6">
              Create First Cellar
            </Button>
          </Card>
        )}
      </div>
    </div>
  )
}

export default CellarOverview
