# Winecellar Frontend Template

A premium wine cellar management application built with React, Vite, and Tailwind CSS, styled with the Winecellar Design System.

## Features

- **Landing Page**: Elegant marketing home with clear call-to-action
- **Sign In**: Mocked authentication (accepts any username/password combination)
- **Cellar Overview**: Browse all your wine cellars with visual cards
- **Cellar View**: Detailed inventory of bottles in each cellar with expandable details
- **Design System Integration**: Full implementation of the Winecellar color palette, typography, spacing, and component styles
- **Responsive Layout**: Mobile-first design that scales beautifully to desktop

## Quick Start

### Prerequisites

- Node.js 16+ and npm

### Installation

```bash
cd frontend-template
npm install
```

### Development

```bash
npm run dev
```

The app will open automatically at `http://localhost:5173`.

### Build

```bash
npm run build
```

## Project Structure

```
src/
├── components/
│   └── index.jsx          # Reusable UI components (Button, Card, Input, NavBar, etc.)
├── pages/
│   ├── Landing.jsx        # Marketing landing page
│   ├── SignIn.jsx         # Authentication page (mock)
│   ├── CellarOverview.jsx # List of cellars
│   └── CellarView.jsx     # Bottles in a cellar
├── mockData.js            # Mock cellars and bottles data
├── App.jsx                # Main router and auth state
├── main.jsx               # Entry point
└── index.css              # Global styles with Tailwind

Config Files:
├── tailwind.config.js     # Tailwind configuration with design system tokens
├── vite.config.js         # Vite configuration
├── postcss.config.js      # PostCSS configuration
├── package.json           # Dependencies and scripts
└── index.html             # HTML template with Google Fonts
```

## Design System

The template fully implements the Winecellar Design System with:

- **Color Palette**: Warm, wine-forward neutrals with branded primary (burgundy) and secondary (bronze) accents
- **Typography**: Cormorant Garamond for display, Manrope for UI
- **Spacing**: 4px base unit with semantic scale
- **Components**: Button variants (primary, secondary, tertiary, danger), Cards, Input fields, Headers, Navigation

All design tokens are configured in `tailwind.config.js` and can be easily customized.

## Authentication Flow

The sign-in page accepts **all username/password combinations** for demonstration:

1. User enters any username and password
2. Click "Sign In"
3. Redirected to `/cellars` (cellar overview)
4. All subsequent protected routes require authentication

To sign out, click the user menu in the top-right corner.

## Mock Data

The app includes mock data for 3 cellars with bottles:

- **Bordeaux Reserve**: 3 bottles from Pauillac, Margaux
- **Burgundy Selection**: 2 bottles from Burgundy regions
- **Italian Treasures**: 3 bottles from Tuscany and Piedmont

Bottles include detailed information: vintage, region, grape varieties, alcohol content, and storage position.

## Customization

### Colors

Edit `tailwind.config.js` to customize the design system colors:

```js
colors: {
  "brand-primary": "#6E1F32",  // Change burgundy
  "brand-secondary": "#A36A2C", // Change bronze
  // ... other colors
}
```

### Typography

Update font families and sizes in `tailwind.config.js`:

```js
fontFamily: {
  display: '"Cormorant Garamond", serif',
  body: '"Manrope", sans-serif',
}
```

### Add New Pages

1. Create a new file in `src/pages/`
2. Add a route in `src/App.jsx`
3. Use existing components for consistency

### Add New Components

Create reusable components in `src/components/` and export from `src/components/index.jsx`.

## Component API

### Button

```jsx
<Button variant="primary" size="md">Click me</Button>
// Variants: primary, secondary, tertiary, danger
// Sizes: sm, md, lg
```

### Card

```jsx
<Card className="custom-class">Content here</Card>
```

### Input

```jsx
<Input 
  label="Field Name" 
  placeholder="Enter value"
  type="email"
  onChange={(e) => setValue(e.target.value)}
/>
```

### NavBar

```jsx
<NavBar user={user} onSignOut={handleSignOut} />
```

### Header

```jsx
<Header 
  title="Page Title"
  subtitle="Optional subtitle"
  action={<Button>Action</Button>}
/>
```

## Next Steps

To extend this template:

1. **Connect a Backend**: Replace mock data with API calls
2. **Add Form Validation**: Enhance input components with validation
3. **Implement Routing Guards**: Add middleware for protected routes
4. **Add More Pages**: Create pages for bottle details, cellar settings, etc.
5. **Dark Mode**: Add dark mode support using Tailwind's dark selector
6. **State Management**: Consider Redux or Zustand for complex state

## Browser Support

- Modern browsers (Chrome, Firefox, Safari, Edge)
- Mobile browsers (iOS Safari, Chrome Android)

## License

Part of the Winecellar project.
