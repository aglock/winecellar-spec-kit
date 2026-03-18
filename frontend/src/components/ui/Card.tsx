import { PropsWithChildren } from 'react';

export function Card({ children }: PropsWithChildren) {
  return (
    <section className="rounded-[20px] border border-[var(--color-border-subtle)] bg-[var(--color-surface-raised)] p-6 shadow-[var(--shadow-card)] md:p-8">
      {children}
    </section>
  );
}
