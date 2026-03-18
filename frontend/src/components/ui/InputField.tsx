import { InputHTMLAttributes } from 'react';

type InputFieldProps = InputHTMLAttributes<HTMLInputElement> & {
  label: string;
  error?: string;
};

export function InputField({ label, error, className = '', ...props }: InputFieldProps) {
  return (
    <label className="flex flex-col gap-2">
      <span className="text-xs font-semibold uppercase tracking-[0.18em] text-[var(--color-text-muted)]">{label}</span>
      <input
        className={`h-12 rounded-[12px] border border-[var(--color-border-subtle)] bg-[var(--color-surface)] px-4 text-base text-[var(--color-text-primary)] outline-none transition focus:border-[var(--color-brand-primary)] focus:ring-4 focus:ring-[var(--color-focus-soft)] ${className}`}
        {...props}
      />
      {error ? <span className="text-sm text-[var(--color-danger)]">{error}</span> : null}
    </label>
  );
}
