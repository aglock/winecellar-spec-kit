import { Link } from 'react-router-dom';
import { Button } from '../../components/ui/Button';
import { Card } from '../../components/ui/Card';
import { landingContent } from './content';

export function LandingPage() {
  return (
    <div>
      <section className="relative overflow-hidden bg-[radial-gradient(circle_at_top,#f4e5ea,transparent_45%),linear-gradient(180deg,#f5f1ea_0%,#efe7dc_100%)]">
        <div className="mx-auto grid max-w-6xl gap-12 px-6 py-16 md:grid-cols-[1.2fr_0.8fr] md:px-10 md:py-24">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.22em] text-[var(--color-brand-secondary)]">
              {landingContent.eyebrow}
            </p>
            <h1 className="mt-4 max-w-3xl font-display text-6xl leading-[0.96] text-[var(--color-brand-primary)] md:text-7xl">
              {landingContent.title}
            </h1>
            <p className="mt-6 max-w-2xl text-lg leading-8 text-[var(--color-text-secondary)]">
              {landingContent.body}
            </p>
            <div className="mt-8 flex flex-wrap gap-4">
              <Link to="/register">
                <Button>Register</Button>
              </Link>
              <Link to="/sign-in">
                <Button variant="secondary">Sign In</Button>
              </Link>
            </div>
          </div>
          <Card>
            <div className="space-y-6">
              <p className="text-xs font-semibold uppercase tracking-[0.18em] text-[var(--color-text-muted)]">What this foundation includes</p>
              <ul className="space-y-5">
                {landingContent.benefits.map((benefit) => (
                  <li key={benefit.title}>
                    <h2 className="text-xl font-semibold text-[var(--color-text-primary)]">{benefit.title}</h2>
                    <p className="mt-2 text-sm leading-6 text-[var(--color-text-secondary)]">{benefit.description}</p>
                  </li>
                ))}
              </ul>
            </div>
          </Card>
        </div>
      </section>

      <section className="mx-auto max-w-6xl px-6 py-16 md:px-10">
        <div className="grid gap-6 md:grid-cols-3">
          {landingContent.benefits.map((benefit) => (
            <Card key={benefit.title}>
              <h2 className="text-2xl font-semibold">{benefit.title}</h2>
              <p className="mt-3 text-sm leading-6 text-[var(--color-text-secondary)]">{benefit.description}</p>
            </Card>
          ))}
        </div>
      </section>
    </div>
  );
}
