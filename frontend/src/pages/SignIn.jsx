import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button, Input } from '../components/index'

function SignIn({ onSignIn }) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = (e) => {
    e.preventDefault()
    setIsLoading(true)
    
    // Mock authentication - all credentials accepted
    setTimeout(() => {
      onSignIn(username || 'Guest')
      navigate('/cellars')
    }, 500)
  }

  return (
    <div className="min-h-screen bg-canvas flex items-center justify-center px-4">
      <div className="w-full max-w-md">
        {/* Hero Section */}
        <div className="text-center mb-12">
          <h1 className="display-lg text-brand-primary mb-4">Welcome Back</h1>
          <p className="body-lg text-text-secondary">
            Access your wine collection
          </p>
        </div>

        {/* Sign In Form */}
        <div className="bg-surface-raised rounded-lg border border-border-subtle shadow-md p-8">
          <form onSubmit={handleSubmit} className="space-y-6">
            <Input
              label="Username"
              placeholder="Enter your username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            
            <Input
              label="Password"
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <Button 
              variant="primary" 
              size="lg" 
              className="w-full"
              disabled={isLoading}
            >
              {isLoading ? 'Signing in...' : 'Sign In'}
            </Button>
          </form>

          <div className="mt-6 text-center">
            <p className="body-sm text-text-muted">
              All credentials are accepted for demo purposes
            </p>
          </div>
        </div>

        {/* Footer Link */}
        <div className="mt-8 text-center">
          <a href="/" className="body-sm text-brand-primary hover:text-brand-primary-hover">
            ← Back to home
          </a>
        </div>
      </div>
    </div>
  )
}

export default SignIn
