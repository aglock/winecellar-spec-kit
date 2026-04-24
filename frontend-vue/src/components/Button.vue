<script setup>
const props = defineProps({
  variant: { type: String, default: 'primary' },
  size: { type: String, default: 'md' },
})

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
</script>

<template>
  <button
    :class="[
      'font-semibold rounded transition-colors duration-180 focus:outline-none focus:ring-2 focus:ring-focus',
      variants[props.variant],
      sizes[props.size],
    ]"
  >
    <slot />
  </button>
</template>
