import { useEffect, useState } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { Card } from '../../components/ui/Card';
import { StatusPanel } from '../../components/ui/StatusPanel';
import { Button } from '../../components/ui/Button';
import { activateAccount, ApiError } from './auth-api';

export function ActivatePage() {
  const [searchParams] = useSearchParams();
  const [state, setState] = useState<'loading' | 'success' | 'error'>('loading');
  const [message, setMessage] = useState('Checking the activation link.');

  useEffect(() => {
    const token = searchParams.get('token');
    if (!token) {
      setState('error');
      setMessage('The activation link is missing its token.');
      return;
    }

    let cancelled = false;
    void activateAccount(token)
      .then(() => {
        if (!cancelled) {
          setState('success');
          setMessage('Your account is now active. You can sign in.');
        }
      })
      .catch((error) => {
        if (!cancelled) {
          setState('error');
          setMessage(error instanceof ApiError ? error.message : 'Activation failed.');
        }
      });

    return () => {
      cancelled = true;
    };
  }, [searchParams]);

  return (
    <div className="mx-auto max-w-xl">
      <Card>
        <h1 className="font-display text-5xl text-[var(--color-brand-primary)]">Activate account</h1>
        <div className="mt-6">
          <StatusPanel
            tone={state === 'success' ? 'success' : state === 'error' ? 'danger' : 'neutral'}
            title={state === 'loading' ? 'Processing link' : state === 'success' ? 'Account activated' : 'Activation unavailable'}
          >
            {message}
          </StatusPanel>
        </div>
        <div className="mt-8 flex gap-4">
          <Link to="/sign-in">
            <Button>Go to sign in</Button>
          </Link>
          <Link to="/register">
            <Button variant="secondary">Register again</Button>
          </Link>
        </div>
      </Card>
    </div>
  );
}
