import { Link } from 'react-router-dom';
import { ReactNode } from 'react';
import { Button } from '../ui/Button';
import { useAuth } from '../../lib/auth/auth-store';

type AppShellProps = {
  children: ReactNode;
};

export function AppShell({ children }: AppShellProps) {
  const { session, signOut } = useAuth();

  return (
    <div className="min-h-screen bg-[var(--color-canvas)] text-[var(--color-text-primary)]">
      <header className="sticky top-0 z-20 border-b border-[var(--color-border-subtle)] bg-[var(--color-surface-raised)]">
        <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4 md:px-10">
          <Link to="/cellars" className="font-display text-3xl text-[var(--color-brand-primary)]">
            Winecellar
          </Link>
          <div className="flex items-center gap-4">
            <div className="hidden text-right md:block">
              <p className="text-sm uppercase tracking-[0.18em] text-[var(--color-text-muted)]">Signed in</p>
              <p className="font-semibold">{session?.user.displayName ?? session?.user.email}</p>
            </div>
            <Button variant="secondary" onClick={() => void signOut()}>
              Sign Out
            </Button>
          </div>
        </div>
      </header>
      <main className="mx-auto max-w-6xl px-6 py-12 md:px-10">{children}</main>
    </div>
  );
}
