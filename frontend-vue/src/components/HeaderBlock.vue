<script setup>
defineProps({
  title: { type: String, required: true },
  subtitle: { type: String, default: '' },
})
</script>

<template>
  <div class="flex justify-between items-start gap-4 mb-8">
    <div>
      <h1 class="heading-lg text-text-primary">{{ title }}</h1>
      <p v-if="subtitle" class="body-md text-text-secondary mt-2">{{ subtitle }}</p>
    </div>
    <div>
      <slot name="action" />
    </div>
  </div>
</template>
