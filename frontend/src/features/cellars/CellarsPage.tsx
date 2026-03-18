import { Card } from '../../components/ui/Card';
import { StatusPanel } from '../../components/ui/StatusPanel';
import { useCellarsQuery } from './useCellarsQuery';

export function CellarsPage() {
  const { data, loading, error } = useCellarsQuery();

  return (
    <div>
      <div className="mb-8 flex flex-col gap-3 md:flex-row md:items-end md:justify-between">
        <div>
          <p className="text-xs font-semibold uppercase tracking-[0.18em] text-[var(--color-text-muted)]">Cellars</p>
          <h1 className="mt-3 font-display text-6xl text-[var(--color-brand-primary)]">Your cellar universe.</h1>
        </div>
        <p className="max-w-xl text-sm leading-6 text-[var(--color-text-secondary)]">
          Membership controls what appears here. Owners, contributors, and viewers all see the cellars they can access.
        </p>
      </div>

      {loading ? (
        <StatusPanel title="Loading cellars">Checking your memberships and current cellar summaries.</StatusPanel>
      ) : error ? (
        <StatusPanel tone="danger" title="Unable to load cellars">
          {error}
        </StatusPanel>
      ) : data.length === 0 ? (
        <Card>
          <p className="text-xs font-semibold uppercase tracking-[0.18em] text-[var(--color-brand-secondary)]">Empty collection</p>
          <h2 className="mt-3 text-3xl font-semibold">No accessible cellars yet.</h2>
          <p className="mt-4 max-w-2xl text-sm leading-6 text-[var(--color-text-secondary)]">
            You are signed in, but there are no cellar memberships to show yet. Once a cellar is shared with you, it will appear here with your role and summary details.
          </p>
        </Card>
      ) : (
        <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          {data.map((cellar) => (
            <Card key={cellar.cellarId}>
              <p className="text-xs font-semibold uppercase tracking-[0.18em] text-[var(--color-brand-secondary)]">{cellar.memberRole}</p>
              <h2 className="mt-3 text-2xl font-semibold">{cellar.name}</h2>
              <p className="mt-2 text-sm text-[var(--color-text-secondary)]">{cellar.location || 'Location not specified'}</p>
              <p className="mt-4 text-sm leading-6 text-[var(--color-text-secondary)]">{cellar.description || 'No description yet.'}</p>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
