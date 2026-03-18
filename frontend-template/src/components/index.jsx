import React from 'react'

export const Button = ({ 
  children, 
  variant = 'primary', 
  size = 'md', 
  className = '',
  ...props 
}) => {
  const baseStyles = 'font-semibold rounded transition-colors duration-180 focus:outline-none focus:ring-2 focus:ring-focus'
  
  const variants = {
    primary: 'bg-brand-primary text-text-inverse hover:bg-brand-primary-hover active:bg-brand-primary-active',
    secondary: 'bg-surface-raised border border-border-strong text-text-primary hover:bg-surface-tinted',
    tertiary: 'text-brand-primary hover:bg-brand-primary-soft',
    danger: 'bg-danger text-text-inverse hover:bg-red-700',
  }

  const sizes = {
    sm: 'px-3 py-2 text-body-sm',
    md: 'px-4 py-3 text-body-md',
    lg: 'px-6 py-4 text-body-md',
  }

  return (
    <button 
      className={`${baseStyles} ${variants[variant]} ${sizes[size]} ${className}`}
      {...props}
    >
      {children}
    </button>
  )
}

export const Card = ({ children, className = '' }) => {
  return (
    <div className={`bg-surface-raised rounded-lg border border-border-subtle shadow-sm p-6 ${className}`}>
      {children}
    </div>
  )
}

export const Header = ({ title, subtitle, action }) => {
  return (
    <div className="flex justify-between items-start gap-4 mb-8">
      <div>
        <h1 className="heading-lg text-text-primary">{title}</h1>
        {subtitle && <p className="body-md text-text-secondary mt-2">{subtitle}</p>}
      </div>
      {action && <div>{action}</div>}
    </div>
  )
}

export const Input = ({ label, placeholder, type = 'text', className = '', ...props }) => {
  return (
    <div className="mb-4">
      {label && <label className="label text-text-primary block mb-2">{label}</label>}
      <input
        type={type}
        placeholder={placeholder}
        className={`w-full px-4 py-3 text-body-md rounded-sm border border-border-subtle bg-surface-raised text-text-primary placeholder-text-muted focus:outline-none focus:border-brand-primary focus:ring-2 focus:ring-focus transition-colors ${className}`}
        {...props}
      />
    </div>
  )
}

export const UserMenu = ({ user, onSignOut }) => {
  const [isOpen, setIsOpen] = React.useState(false)

  return (
    <div className="relative">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="px-3 py-2 rounded-lg bg-surface-raised border border-border-subtle text-text-primary hover:bg-surface-tinted transition-colors"
      >
        {user?.username || 'Menu'}
      </button>
      {isOpen && (
        <div className="absolute right-0 mt-2 w-48 bg-surface-raised border border-border-subtle rounded-lg shadow-md z-10">
          <div className="p-4 border-b border-border-subtle">
            <p className="body-sm text-text-primary">{user?.email}</p>
          </div>
          <button
            onClick={() => {
              onSignOut()
              setIsOpen(false)
            }}
            className="w-full text-left px-4 py-3 text-body-sm text-text-primary hover:bg-surface-tinted transition-colors"
          >
            Sign Out
          </button>
        </div>
      )}
    </div>
  )
}

export const NavBar = ({ user, onSignOut }) => {
  return (
    <nav className="bg-surface-raised border-b border-border-subtle sticky top-0 z-20">
      <div className="max-w-6xl mx-auto px-8 py-4 flex justify-between items-center">
        <h1 className="title text-brand-primary font-display">Winecellar</h1>
        {user && <UserMenu user={user} onSignOut={onSignOut} />}
      </div>
    </nav>
  )
}
