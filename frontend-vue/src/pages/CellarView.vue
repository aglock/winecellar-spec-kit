<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { NavBar, HeaderBlock, Card, Button, AsyncStateMessage } from '../components'
import { listDefaultCellarBottles } from '../services/bottleService'
import { sessionState, signOut } from '../state/session'

const route = useRoute()
const router = useRouter()

const bottles = ref([])
const isLoading = ref(true)
const loadError = ref('')
const page = ref(0)
const size = ref(50)
const total = ref(0)
const selectedBottleId = ref(null)

const user = computed(() => ({ username: 'Collector', email: sessionState.user?.email || '' }))
const cellarId = computed(() => route.params.cellarId)

async function loadBottles() {
  isLoading.value = true
  loadError.value = ''
  try {
    const response = await listDefaultCellarBottles({ page: page.value, size: size.value })
    bottles.value = response.items || []
    total.value = response.total || 0
  } catch (error) {
    loadError.value = error.message || 'Failed to load bottles'
  } finally {
    isLoading.value = false
  }
}

function toggleBottleDetails(bottleId) {
  selectedBottleId.value = selectedBottleId.value === bottleId ? null : bottleId
}

function handleSignOut() {
  signOut()
  router.push('/')
}

onMounted(() => {
  if (cellarId.value === 'default') {
    loadBottles()
  }
})

watch(
  () => [page.value, sessionState.bottleListRefreshToken],
  () => {
    if (cellarId.value === 'default') {
      loadBottles()
    }
  },
)
</script>

<template>
  <div v-if="cellarId && cellarId !== 'default'" class="min-h-screen bg-canvas flex items-center justify-center">
    <div class="text-center">
      <h1 class="heading-lg text-text-primary mb-4">Cellar Not Found</h1>
      <RouterLink to="/cellars">
        <Button variant="primary">Back to Cellars</Button>
      </RouterLink>
    </div>
  </div>

  <div v-else class="min-h-screen bg-canvas">
    <NavBar :user="user" @sign-out="handleSignOut" />

    <div class="max-w-6xl mx-auto px-8 py-12">
      <div class="bg-surface-raised rounded-lg border border-border-subtle p-8 mb-8">
        <div class="flex justify-between items-start mb-4">
          <div>
            <RouterLink to="/cellars" class="body-sm text-brand-primary hover:text-brand-primary-hover mb-2 block">
              ← Back to Cellars
            </RouterLink>
            <h1 class="display-lg text-brand-primary">Default Cellar</h1>
            <p class="body-lg text-text-secondary mt-2">Imported bottle inventory</p>
          </div>
          <div class="text-right">
            <div class="display-md text-brand-secondary">{{ total }}</div>
            <p class="body-sm text-text-muted">bottles</p>
          </div>
        </div>
        <p class="body-md text-text-secondary">Browse and verify bottles imported from your CSV source.</p>
      </div>

      <HeaderBlock title="Inventory" subtitle="All bottles currently mapped to the default cellar" />

      <Card v-if="isLoading" class="mb-4">
        <AsyncStateMessage
          type="info"
          title="Loading inventory"
          message="Fetching your latest bottle list from the cellar service."
        />
      </Card>

      <Card v-if="loadError" class="mb-4 border-danger">
        <AsyncStateMessage type="error" title="Could not load bottles" :message="loadError" />
      </Card>

      <div v-if="!isLoading && !loadError && bottles.length > 0" class="grid grid-cols-1 gap-4">
        <Card
          v-for="bottle in bottles"
          :key="bottle.bottleId"
          class="cursor-pointer hover:shadow-md transition-shadow hover:border-brand-primary"
          @click="toggleBottleDetails(bottle.bottleId)"
        >
          <div class="grid grid-cols-1 md:grid-cols-12 gap-6 items-start">
            <div class="md:col-span-7">
              <h3 class="heading-sm text-text-primary mb-2">{{ bottle.wineName }}</h3>
              <div class="flex flex-wrap gap-3 mb-4">
                <span class="label text-brand-secondary bg-brand-secondary-soft px-3 py-1 rounded-full">{{ bottle.vintageDisplay }}</span>
                <span class="label text-accent-olive bg-accent-olive-soft px-3 py-1 rounded-full">{{ bottle.wineType }}</span>
                <span class="label text-text-muted bg-surface-tinted px-3 py-1 rounded-full">{{ bottle.region }}</span>
              </div>
              <p class="body-sm text-text-secondary mb-2"><strong>Producer:</strong> {{ bottle.producerName }}</p>
              <p class="body-sm text-text-muted">{{ bottle.country }} • {{ bottle.bottleSize }}</p>
            </div>

            <div class="md:col-span-5 space-y-4 md:border-l md:border-border-subtle md:pl-6">
              <div>
                <p class="label text-text-muted mb-1">Quantity</p>
                <p class="heading-sm text-text-primary">{{ bottle.quantity }}</p>
              </div>
            </div>
          </div>

          <div
            v-if="selectedBottleId === bottle.bottleId"
            class="mt-6 pt-6 border-t border-border-subtle bg-surface-tinted p-4 rounded-md"
          >
            <h4 class="title text-text-primary mb-3">Additional Information</h4>
            <div class="grid grid-cols-2 md:grid-cols-3 gap-4 text-body-sm">
              <div>
                <p class="label text-text-muted">Bottle ID</p>
                <p class="text-text-primary break-all">{{ bottle.bottleId }}</p>
              </div>
              <div>
                <p class="label text-text-muted">Country</p>
                <p class="text-text-primary">{{ bottle.country }}</p>
              </div>
              <div>
                <p class="label text-text-muted">Size</p>
                <p class="text-text-primary">{{ bottle.bottleSize }}</p>
              </div>
            </div>
          </div>
        </Card>
      </div>

      <Card v-else-if="!isLoading && !loadError" class="text-center py-12 bg-brand-primary-soft border-brand-primary">
        <div class="text-6xl mb-4">🍾</div>
        <AsyncStateMessage
          type="warning"
          title="No bottles yet"
          message="Import a CSV file from the cellar overview to populate this list."
        />
      </Card>

      <div v-if="!isLoading && !loadError" class="flex items-center justify-between mt-6">
        <Button
          variant="secondary"
          size="sm"
          :disabled="page === 0"
          @click="page = Math.max(0, page - 1)"
        >
          Previous
        </Button>
        <p class="body-sm text-text-secondary">Page {{ page + 1 }}</p>
        <Button
          variant="secondary"
          size="sm"
          :disabled="(page + 1) * size >= total"
          @click="page += 1"
        >
          Next
        </Button>
      </div>
    </div>
  </div>
</template>
