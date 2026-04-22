import React, { useState } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Landing from './pages/Landing'
import SignIn from './pages/SignIn'
import CellarOverview from './pages/CellarOverview'
import CellarView from './pages/CellarView'

const AUTH_STORAGE_KEY = 'winecellar.authUser'

function loadStoredUser() {
  const rawValue = window.localStorage.getItem(AUTH_STORAGE_KEY)
  if (!rawValue) {
    return null
  }

  try {
    return JSON.parse(rawValue)
  } catch {
    window.localStorage.removeItem(AUTH_STORAGE_KEY)
    return null
  }
}

function App() {
  const [user, setUser] = useState(() => loadStoredUser())
  const [bottleListRefreshToken, setBottleListRefreshToken] = useState(0)
  const isAuthenticated = user !== null

  const handleSignIn = (username) => {
    const authenticatedUser = { username, email: `${username}@example.com` }
    setUser(authenticatedUser)
    window.localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authenticatedUser))
  }

  const handleSignOut = () => {
    setUser(null)
    window.localStorage.removeItem(AUTH_STORAGE_KEY)
  }

  const handleImportCompleted = () => {
    setBottleListRefreshToken((current) => current + 1)
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
          element={isAuthenticated
            ? <CellarOverview user={user} onSignOut={handleSignOut} onImportCompleted={handleImportCompleted} />
            : <Navigate to="/" />}
        />
        <Route 
          path="/cellars/:cellarId" 
          element={isAuthenticated
            ? <CellarView onSignOut={handleSignOut} refreshToken={bottleListRefreshToken} />
            : <Navigate to="/" />}
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App
