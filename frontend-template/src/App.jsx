import React, { useState } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Landing from './pages/Landing'
import SignIn from './pages/SignIn'
import CellarOverview from './pages/CellarOverview'
import CellarView from './pages/CellarView'

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [user, setUser] = useState(null)

  const handleSignIn = (username) => {
    setIsAuthenticated(true)
    setUser({ username, email: `${username}@example.com` })
  }

  const handleSignOut = () => {
    setIsAuthenticated(false)
    setUser(null)
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={isAuthenticated ? <Navigate to="/cellars" /> : <Landing />} />
        <Route 
          path="/signin" 
          element={isAuthenticated ? <Navigate to="/cellars" /> : <SignIn onSignIn={handleSignIn} />} 
        />
        <Route 
          path="/cellars" 
          element={isAuthenticated ? <CellarOverview user={user} onSignOut={handleSignOut} /> : <Navigate to="/" />} 
        />
        <Route 
          path="/cellars/:cellarId" 
          element={isAuthenticated ? <CellarView onSignOut={handleSignOut} /> : <Navigate to="/" />} 
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App
