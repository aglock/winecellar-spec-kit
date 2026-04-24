import { reactive } from 'vue'

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

export const sessionState = reactive({
  user: loadStoredUser(),
  bottleListRefreshToken: 0,
})

export function isAuthenticated() {
  return sessionState.user !== null
}

export function signIn(username) {
  const authenticatedUser = { username, email: `${username}@example.com` }
  sessionState.user = authenticatedUser
  window.localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authenticatedUser))
}

export function signOut() {
  sessionState.user = null
  window.localStorage.removeItem(AUTH_STORAGE_KEY)
}

export function notifyImportCompleted() {
  sessionState.bottleListRefreshToken += 1
}
