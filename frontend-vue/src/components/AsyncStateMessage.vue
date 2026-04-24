<script setup>
import { computed } from 'vue'

const props = defineProps({
  type: { type: String, default: 'info' },
  title: { type: String, default: '' },
  message: { type: String, default: '' },
})

const palette = {
  info: 'bg-surface-raised border-border-subtle text-text-secondary',
  success: 'bg-brand-secondary-soft border-brand-secondary text-text-primary',
  warning: 'bg-brand-primary-soft border-brand-primary text-text-primary',
  error: 'bg-surface-raised border-danger text-danger',
}

const messageClass = computed(() => palette[props.type] || palette.info)
</script>

<template>
  <div :class="['rounded-lg border p-4', messageClass]">
    <p v-if="title" class="heading-sm mb-1">{{ title }}</p>
    <p class="body-sm">{{ message }}</p>
  </div>
</template>
