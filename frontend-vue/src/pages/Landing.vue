<script setup>
import { RouterLink } from 'vue-router'
import { Button } from '../components'
</script>

<template>
  <div class="min-h-screen bg-canvas">
    <nav class="bg-surface-raised border-b border-border-subtle sticky top-0 z-20">
      <div class="max-w-6xl mx-auto px-8 py-4 flex justify-between items-center">
        <h1 class="title text-brand-primary font-display">Winecellar</h1>
        <div class="flex gap-3">
          <RouterLink to="/signin">
            <Button variant="secondary">Sign In</Button>
          </RouterLink>
        </div>
      </div>
    </nav>

    <section class="max-w-6xl mx-auto px-8 py-20">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-12 items-center">
        <div>
          <h1 class="display-xl text-brand-primary mb-6 leading-tight">
            Your Wine Collection, <span class="text-brand-secondary">Perfectly Organized</span>
          </h1>
          <p class="body-lg text-text-secondary mb-8 max-w-lg leading-relaxed">
            Winecellar transforms how you manage your collection. Track bottles, note tastings, and share your passion with fellow collectors.
          </p>
          <div class="flex gap-4">
            <RouterLink to="/signin">
              <Button variant="primary" size="lg">Get Started</Button>
            </RouterLink>
            <Button variant="secondary" size="lg">Learn More</Button>
          </div>
        </div>

        <div class="bg-surface-tinted rounded-lg border border-border-subtle h-96 flex items-center justify-center">
          <div class="text-center">
            <div class="text-6xl mb-4">🍷</div>
            <p class="body-md text-text-muted">Your cellar awaits</p>
          </div>
        </div>
      </div>
    </section>

    <section class="bg-surface-raised mt-20 py-20">
      <div class="max-w-6xl mx-auto px-8">
        <h2 class="heading-lg text-center text-text-primary mb-12">The Complete Cellar Solution</h2>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="bg-canvas rounded-lg border border-border-subtle p-8">
            <div class="text-4xl mb-4">📋</div>
            <h3 class="heading-sm text-text-primary mb-3">Organize Your Collection</h3>
            <p class="body-md text-text-secondary">Create cellars, catalog bottles, and track storage conditions with precision.</p>
          </div>

          <div class="bg-canvas rounded-lg border border-border-subtle p-8">
            <div class="text-4xl mb-4">📝</div>
            <h3 class="heading-sm text-text-primary mb-3">Record Your Journey</h3>
            <p class="body-md text-text-secondary">Note tasting dates, decanting recommendations, and personal observations.</p>
          </div>

          <div class="bg-canvas rounded-lg border border-border-subtle p-8">
            <div class="text-4xl mb-4">🤝</div>
            <h3 class="heading-sm text-text-primary mb-3">Share & Collaborate</h3>
            <p class="body-md text-text-secondary">Invite members to cellars and build shared collections with friends.</p>
          </div>
        </div>
      </div>
    </section>

    <section class="bg-brand-primary text-text-inverse py-20">
      <div class="max-w-6xl mx-auto px-8 text-center">
        <h2 class="display-lg mb-6">Start Cataloging Today</h2>
        <p class="body-lg mb-8 max-w-2xl mx-auto">
          Join thousands of collectors who trust Winecellar with their most treasured bottles.
        </p>
        <RouterLink to="/signin">
          <Button variant="primary" size="lg" class="!bg-text-inverse !text-brand-primary hover:!bg-surface-raised">
            Sign In Now
          </Button>
        </RouterLink>
      </div>
    </section>

    <footer class="bg-surface-raised border-t border-border-subtle py-8">
      <div class="max-w-6xl mx-auto px-8">
        <p class="body-sm text-text-muted text-center">© 2024 Winecellar. A premium collection management platform.</p>
      </div>
    </footer>
  </div>
</template>
