<script setup>
defineProps({
  label: { type: String, default: '' },
  placeholder: { type: String, default: '' },
  type: { type: String, default: 'text' },
  modelValue: { type: String, default: '' },
})

const emit = defineEmits(['update:modelValue'])
</script>

<template>
  <div class="mb-4">
    <label v-if="label" class="label text-text-primary block mb-2">{{ label }}</label>
    <input
      :type="type"
      :placeholder="placeholder"
      :value="modelValue"
      class="w-full px-4 py-3 text-body-md rounded-sm border border-border-subtle bg-surface-raised text-text-primary placeholder-text-muted focus:outline-none focus:border-brand-primary focus:ring-2 focus:ring-focus transition-colors"
      @input="emit('update:modelValue', $event.target.value)"
    />
  </div>
</template>
