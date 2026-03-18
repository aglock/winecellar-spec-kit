import { Link } from 'react-router-dom';
import { ReactNode } from 'react';
import { Button } from '../ui/Button';

type PublicShellProps = {
  children: ReactNode;
  compact?: boolean;
};

export function PublicShell({ children, compact = false }: PublicShellProps) {
  return (
    <div className="min-h-screen bg-[var(--color-canvas)] text-[var(--color-text-primary)]">
      <header className="sticky top-0 z-20 border-b border-[var(--color-border-subtle)] bg-[color:rgba(255,253,252,0.94)] backdrop-blur">
        <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4 md:px-10">
          <Link to="/" className="font-display text-3xl text-[var(--color-brand-primary)]">
            Winecellar
          </Link>
          <div className="flex items-center gap-3">
            <Link to="/register">
              <Button variant="secondary">Register</Button>
            </Link>
            <Link to="/sign-in">
              <Button>Sign In</Button>
            </Link>
          </div>
        </div>
      </header>
      <main className={compact ? 'mx-auto max-w-4xl px-6 py-16 md:px-10' : ''}>{children}</main>
    </div>
  );
}
