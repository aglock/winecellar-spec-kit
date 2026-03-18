import { FormEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Card } from '../../components/ui/Card';
import { InputField } from '../../components/ui/InputField';
import { Button } from '../../components/ui/Button';
import { StatusPanel } from '../../components/ui/StatusPanel';
import { ApiError } from './auth-api';
import { useAuth } from '../../lib/auth/auth-store';

export function SignInPage() {
  const navigate = useNavigate();
  const { signIn } = useAuth();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      await signIn(form.email, form.password);
      navigate('/cellars');
    } catch (submitError) {
      setError(submitError instanceof ApiError ? submitError.message : 'Sign-in failed.');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="mx-auto max-w-xl">
      <Card>
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-[var(--color-text-muted)]">Trusted re-entry</p>
        <h1 className="mt-3 font-display text-5xl text-[var(--color-brand-primary)]">Welcome back.</h1>
        <form className="mt-8 space-y-5" onSubmit={(event) => void handleSubmit(event)}>
          <InputField
            label="Email"
            type="email"
            value={form.email}
            onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))}
            required
          />
          <InputField
            label="Password"
            type="password"
            value={form.password}
            onChange={(event) => setForm((current) => ({ ...current, password: event.target.value }))}
            required
          />
          <Button className="w-full" disabled={submitting}>
            {submitting ? 'Signing in...' : 'Sign In'}
          </Button>
        </form>
        {error ? (
          <div className="mt-6">
            <StatusPanel tone="danger" title="Sign-in unavailable">
              {error}
            </StatusPanel>
          </div>
        ) : null}
        <p className="mt-6 text-sm text-[var(--color-text-secondary)]">
          Need access? <Link to="/register" className="text-[var(--color-brand-primary)] underline">Register here</Link>.
        </p>
      </Card>
    </div>
  );
}
