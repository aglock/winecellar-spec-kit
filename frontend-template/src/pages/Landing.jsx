import React from 'react'
import { Link } from 'react-router-dom'
import { Button } from '../components/index'

function Landing() {
  return (
    <div className="min-h-screen bg-canvas">
      {/* Navigation */}
      <nav className="bg-surface-raised border-b border-border-subtle sticky top-0 z-20">
        <div className="max-w-6xl mx-auto px-8 py-4 flex justify-between items-center">
          <h1 className="title text-brand-primary font-display">Winecellar</h1>
          <div className="flex gap-3">
            <Link to="/signin">
              <Button variant="secondary">Sign In</Button>
            </Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="max-w-6xl mx-auto px-8 py-20">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-12 items-center">
          <div>
            <h1 className="display-xl text-brand-primary mb-6 leading-tight">
              Your Wine Collection, <span className="text-brand-secondary">Perfectly Organized</span>
            </h1>
            <p className="body-lg text-text-secondary mb-8 max-w-lg leading-relaxed">
              Winecellar transforms how you manage your collection. Track bottles, note tastings, and share your passion with fellow collectors.
            </p>
            <div className="flex gap-4">
              <Link to="/signin">
                <Button variant="primary" size="lg">
                  Get Started
                </Button>
              </Link>
              <Button variant="secondary" size="lg">
                Learn More
              </Button>
            </div>
          </div>
          
          {/* Illustration placeholder */}
          <div className="bg-surface-tinted rounded-lg border border-border-subtle h-96 flex items-center justify-center">
            <div className="text-center">
              <div className="text-6xl mb-4">🍷</div>
              <p className="body-md text-text-muted">Your cellar awaits</p>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="bg-surface-raised mt-20 py-20">
        <div className="max-w-6xl mx-auto px-8">
          <h2 className="heading-lg text-center text-text-primary mb-12">
            The Complete Cellar Solution
          </h2>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Feature 1 */}
            <div className="bg-canvas rounded-lg border border-border-subtle p-8">
              <div className="text-4xl mb-4">📋</div>
              <h3 className="heading-sm text-text-primary mb-3">Organize Your Collection</h3>
              <p className="body-md text-text-secondary">
                Create cellars, catalog bottles, and track storage conditions with precision.
              </p>
            </div>

            {/* Feature 2 */}
            <div className="bg-canvas rounded-lg border border-border-subtle p-8">
              <div className="text-4xl mb-4">📝</div>
              <h3 className="heading-sm text-text-primary mb-3">Record Your Journey</h3>
              <p className="body-md text-text-secondary">
                Note tasting dates, decanting recommendations, and personal observations.
              </p>
            </div>

            {/* Feature 3 */}
            <div className="bg-canvas rounded-lg border border-border-subtle p-8">
              <div className="text-4xl mb-4">🤝</div>
              <h3 className="heading-sm text-text-primary mb-3">Share & Collaborate</h3>
              <p className="body-md text-text-secondary">
                Invite members to cellars and build shared collections with friends.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="bg-brand-primary text-text-inverse py-20">
        <div className="max-w-6xl mx-auto px-8 text-center">
          <h2 className="display-lg mb-6">Start Cataloging Today</h2>
          <p className="body-lg mb-8 max-w-2xl mx-auto">
            Join thousands of collectors who trust Winecellar with their most treasured bottles.
          </p>
          <Link to="/signin">
            <Button variant="primary" size="lg" className="!bg-text-inverse !text-brand-primary hover:!bg-surface-raised">
              Sign In Now
            </Button>
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-surface-raised border-t border-border-subtle py-8">
        <div className="max-w-6xl mx-auto px-8">
          <p className="body-sm text-text-muted text-center">
            © 2024 Winecellar. A premium collection management platform.
          </p>
        </div>
      </footer>
    </div>
  )
}

export default Landing
