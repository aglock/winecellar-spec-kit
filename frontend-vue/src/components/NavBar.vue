<script setup>
import UserMenu from './UserMenu.vue'

const props = defineProps({
  user: { type: Object, default: null },
})

const emit = defineEmits(['sign-out'])
</script>

<template>
  <nav class="bg-surface-raised border-b border-border-subtle sticky top-0 z-20">
    <div class="max-w-6xl mx-auto px-8 py-4 flex justify-between items-center">
      <h1 class="title text-brand-primary font-display">Winecellar</h1>
      <UserMenu v-if="props.user" :user="props.user" @sign-out="emit('sign-out')" />
    </div>
  </nav>
</template>
