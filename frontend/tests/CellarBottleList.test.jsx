import { describe, expect, it } from 'vitest'
import { render, screen } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import CellarView from '../src/pages/CellarView'

function renderWithRouter(ui) {
  return render(<BrowserRouter>{ui}</BrowserRouter>)
}

describe('Cellar bottle list', () => {
  it('shows empty-state messaging when no bottles are present', () => {
    renderWithRouter(<CellarView onSignOut={() => {}} initialBottles={[]} />)
    expect(screen.getByText(/No bottles yet/i)).toBeInTheDocument()
  })

  it('renders NV display for non-vintage bottles', () => {
    renderWithRouter(
      <CellarView
        onSignOut={() => {}}
        initialBottles={[
          {
            bottleId: 'bottle-1',
            wineName: 'Test Wine',
            producerName: 'Test Producer',
            vintageDisplay: 'NV',
            wineType: 'Sparkling',
            bottleSize: '750ml',
            country: 'France',
            region: 'Champagne',
            quantity: 2,
          },
        ]}
      />
    )

    expect(screen.getByText('NV')).toBeInTheDocument()
    expect(screen.getByText('Test Wine')).toBeInTheDocument()
  })
})
