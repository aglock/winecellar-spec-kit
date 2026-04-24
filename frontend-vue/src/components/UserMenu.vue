<script setup>
import { ref } from 'vue'

const props = defineProps({
  user: { type: Object, default: null },
})

const emit = defineEmits(['sign-out'])
const isOpen = ref(false)

function handleSignOut() {
  emit('sign-out')
  isOpen.value = false
}
</script>

<template>
  <div class="relative">
    <button
      class="px-3 py-2 rounded-lg bg-surface-raised border border-border-subtle text-text-primary hover:bg-surface-tinted transition-colors"
      @click="isOpen = !isOpen"
    >
      {{ props.user?.username || 'Menu' }}
    </button>
    <div
      v-if="isOpen"
      class="absolute right-0 mt-2 w-48 bg-surface-raised border border-border-subtle rounded-lg shadow-md z-10"
    >
      <div class="p-4 border-b border-border-subtle">
        <p class="body-sm text-text-primary">{{ props.user?.email }}</p>
      </div>
      <button
        class="w-full text-left px-4 py-3 text-body-sm text-text-primary hover:bg-surface-tinted transition-colors"
        @click="handleSignOut"
      >
        Sign Out
      </button>
    </div>
  </div>
</template>
