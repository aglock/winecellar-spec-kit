<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Button, InputField } from '../components'
import { signIn } from '../state/session'

const username = ref('')
const password = ref('')
const isLoading = ref(false)

const router = useRouter()

function handleSubmit(event) {
  event.preventDefault()
  isLoading.value = true

  setTimeout(() => {
    signIn(username.value || 'Guest')
    router.push('/cellars')
  }, 500)
}
</script>

<template>
  <div class="min-h-screen bg-canvas flex items-center justify-center px-4">
    <div class="w-full max-w-md">
      <div class="text-center mb-12">
        <h1 class="display-lg text-brand-primary mb-4">Welcome Back</h1>
        <p class="body-lg text-text-secondary">Access your wine collection</p>
      </div>

      <div class="bg-surface-raised rounded-lg border border-border-subtle shadow-md p-8">
        <form class="space-y-6" @submit="handleSubmit">
          <InputField v-model="username" label="Username" placeholder="Enter your username" />

          <InputField
            v-model="password"
            label="Password"
            type="password"
            placeholder="Enter your password"
          />

          <Button variant="primary" size="lg" class="w-full" :disabled="isLoading">
            {{ isLoading ? 'Signing in...' : 'Sign In' }}
          </Button>
        </form>

        <div class="mt-6 text-center">
          <p class="body-sm text-text-muted">All credentials are accepted for demo purposes</p>
        </div>
      </div>

      <div class="mt-8 text-center">
        <a href="/" class="body-sm text-brand-primary hover:text-brand-primary-hover">← Back to home</a>
      </div>
    </div>
  </div>
</template>
