import { PropsWithChildren } from 'react';

type StatusPanelProps = PropsWithChildren<{
  tone?: 'neutral' | 'success' | 'warning' | 'danger';
  title: string;
}>;

export function StatusPanel({ tone = 'neutral', title, children }: StatusPanelProps) {
  const tones = {
    neutral: 'bg-[var(--color-surface-tinted)] border-[var(--color-border-subtle)]',
    success: 'bg-[var(--color-success-soft)] border-[var(--color-success)]',
    warning: 'bg-[var(--color-warning-soft)] border-[var(--color-warning)]',
    danger: 'bg-[var(--color-danger-soft)] border-[var(--color-danger)]',
  };

  return (
    <div className={`rounded-[16px] border p-4 ${tones[tone]}`}>
      <h3 className="text-lg font-semibold">{title}</h3>
      <div className="mt-2 text-sm text-[var(--color-text-secondary)]">{children}</div>
    </div>
  );
}
