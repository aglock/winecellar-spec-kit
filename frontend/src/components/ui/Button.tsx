import { ButtonHTMLAttributes, PropsWithChildren } from 'react';

type ButtonProps = PropsWithChildren<
  ButtonHTMLAttributes<HTMLButtonElement> & {
    variant?: 'primary' | 'secondary' | 'tertiary';
  }
>;

export function Button({ children, className = '', variant = 'primary', ...props }: ButtonProps) {
  const variants = {
    primary:
      'bg-[var(--color-brand-primary)] text-[var(--color-text-inverse)] shadow-[var(--shadow-button)] hover:bg-[var(--color-brand-primary-hover)]',
    secondary:
      'border border-[var(--color-border-strong)] bg-[var(--color-surface-raised)] text-[var(--color-text-primary)] hover:bg-[var(--color-surface-tinted)]',
    tertiary:
      'bg-transparent text-[var(--color-brand-primary)] hover:bg-[var(--color-brand-primary-soft)]',
  };

  return (
    <button
      className={`inline-flex h-12 items-center justify-center rounded-[14px] px-5 text-sm font-semibold transition duration-180 ease-[cubic-bezier(0.22,1,0.36,1)] focus:outline-none focus:ring-4 focus:ring-[var(--color-focus-soft)] disabled:cursor-not-allowed disabled:opacity-60 ${variants[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
