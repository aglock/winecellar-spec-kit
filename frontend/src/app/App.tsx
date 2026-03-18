import { Routes, Route, Navigate } from 'react-router-dom';
import { ProtectedRoute } from '../routes/ProtectedRoute';
import { AppShell } from '../components/layout/AppShell';
import { PublicShell } from '../components/layout/PublicShell';
import { LandingPage } from '../features/landing/LandingPage';
import { RegisterPage } from '../features/auth/RegisterPage';
import { ActivatePage } from '../features/auth/ActivatePage';
import { SignInPage } from '../features/auth/SignInPage';
import { CellarsPage } from '../features/cellars/CellarsPage';
import { AuthProvider } from '../lib/auth/auth-store';

export function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route
          path="/"
          element={
            <PublicShell>
              <LandingPage />
            </PublicShell>
          }
        />
        <Route
          path="/register"
          element={
            <PublicShell compact>
              <RegisterPage />
            </PublicShell>
          }
        />
        <Route
          path="/activate"
          element={
            <PublicShell compact>
              <ActivatePage />
            </PublicShell>
          }
        />
        <Route
          path="/sign-in"
          element={
            <PublicShell compact>
              <SignInPage />
            </PublicShell>
          }
        />
        <Route
          path="/cellars"
          element={
            <ProtectedRoute>
              <AppShell>
                <CellarsPage />
              </AppShell>
            </ProtectedRoute>
          }
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
}
