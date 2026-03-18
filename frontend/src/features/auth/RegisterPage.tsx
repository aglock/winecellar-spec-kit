import { FormEvent, useState } from 'react';
import { Card } from '../../components/ui/Card';
import { InputField } from '../../components/ui/InputField';
import { Button } from '../../components/ui/Button';
import { StatusPanel } from '../../components/ui/StatusPanel';
import { registerAccount, ApiError } from './auth-api';

export function RegisterPage() {
  const [form, setForm] = useState({ email: '', password: '', displayName: '' });
  const [status, setStatus] = useState<{ ok: boolean; message: string } | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSubmitting(true);
    setStatus(null);
    try {
      const response = await registerAccount(form);
      setStatus({
        ok: true,
        message: `Activation link generated. It expires at ${new Date(response.activationExpiresAt).toLocaleString()}.`,
      });
    } catch (error) {
      const message = error instanceof ApiError ? error.message : 'Registration failed.';
      setStatus({ ok: false, message });
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="mx-auto max-w-xl">
      <Card>
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-[var(--color-text-muted)]">Create account</p>
        <h1 className="mt-3 font-display text-5xl text-[var(--color-brand-primary)]">Open your cellar.</h1>
        <p className="mt-4 text-sm leading-6 text-[var(--color-text-secondary)]">
          Early iterations log activation links in the backend application log. Brevo is planned behind the same delivery path later.
        </p>
        <form className="mt-8 space-y-5" onSubmit={(event) => void handleSubmit(event)}>
          <InputField
            label="Display name"
            value={form.displayName}
            onChange={(event) => setForm((current) => ({ ...current, displayName: event.target.value }))}
            required
          />
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
            minLength={12}
            value={form.password}
            onChange={(event) => setForm((current) => ({ ...current, password: event.target.value }))}
            required
          />
          <Button className="w-full" disabled={submitting}>
            {submitting ? 'Preparing account...' : 'Register'}
          </Button>
        </form>
      </Card>
      {status ? (
        <div className="mt-6">
          <StatusPanel tone={status.ok ? 'success' : 'danger'} title={status.ok ? 'Registration accepted' : 'Registration failed'}>
            {status.message}
          </StatusPanel>
        </div>
      ) : null}
    </div>
  );
}
