import { PropsWithChildren } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { StatusPanel } from '../components/ui/StatusPanel';
import { useAuth } from '../lib/auth/auth-store';

function ProtectedRouteInner({ children }: PropsWithChildren) {
  const { session, bootstrapped } = useAuth();
  const location = useLocation();

  if (!bootstrapped) {
    return (
      <div className="mx-auto max-w-3xl px-6 py-16">
        <StatusPanel title="Checking session">Confirming whether your signed-in session is still active.</StatusPanel>
      </div>
    );
  }

  if (!session) {
    return <Navigate to="/sign-in" replace state={{ redirectTo: location.pathname }} />;
  }

  return children;
}

export function ProtectedRoute({ children }: PropsWithChildren) {
  return <ProtectedRouteInner>{children}</ProtectedRouteInner>;
}
