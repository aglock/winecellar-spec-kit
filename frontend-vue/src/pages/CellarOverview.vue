<script setup>
import { ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { NavBar, HeaderBlock, Card, Button } from '../components'
import { mockCellars } from '../mockData'
import { importWineBottlesCsv } from '../services/importService'
import { sessionState, notifyImportCompleted, signOut } from '../state/session'

const router = useRouter()
const cellars = ref(mockCellars)
const showNewCellar = ref(false)
const selectedFile = ref(null)
const uploadError = ref('')
const isUploading = ref(false)
const importSummary = ref(null)
const uploadForm = ref(null)

async function handleUploadSubmit(event) {
  event.preventDefault()
  uploadError.value = ''

  if (!selectedFile.value) {
    uploadError.value = 'Select a CSV file before uploading.'
    return
  }

  isUploading.value = true
  try {
    const summary = await importWineBottlesCsv(selectedFile.value)
    importSummary.value = summary
    notifyImportCompleted()
    selectedFile.value = null
    uploadForm.value?.reset()
  } catch (error) {
    uploadError.value = error.message || 'Upload failed. Please try again.'
  } finally {
    isUploading.value = false
  }
}

function handleSignOut() {
  signOut()
  router.push('/')
}

function onFileSelected(event) {
  const files = event.target.files
  selectedFile.value = files && files.length > 0 ? files[0] : null
  uploadError.value = ''
}
</script>

<template>
  <div class="min-h-screen bg-canvas">
    <NavBar :user="sessionState.user" @sign-out="handleSignOut" />

    <div class="max-w-6xl mx-auto px-8 py-12">
      <HeaderBlock title="My Cellars" subtitle="Select a cellar to view and manage your bottles">
        <template #action>
          <Button variant="primary" @click="showNewCellar = !showNewCellar">+ New Cellar</Button>
        </template>
      </HeaderBlock>

      <Card class="mb-8 bg-brand-primary-soft border-brand-primary">
        <h3 class="heading-sm text-text-primary mb-2">Import wine bottles (CSV)</h3>
        <p class="body-sm text-text-secondary mb-4">
          Upload your cellar CSV file to add bottles and receive an import summary.
        </p>

        <form ref="uploadForm" class="space-y-4" @submit="handleUploadSubmit">
          <div>
            <label class="label text-text-primary block mb-2" for="csv-upload-input">CSV file</label>
            <input
              id="csv-upload-input"
              type="file"
              accept=".csv,text/csv"
              class="w-full px-4 py-3 rounded-sm border border-border-subtle bg-surface-raised text-text-primary focus:outline-none focus:ring-2 focus:ring-focus"
              @change="onFileSelected"
            >
          </div>

          <div class="flex items-center gap-3">
            <Button variant="primary" size="sm" type="submit" :disabled="isUploading">
              {{ isUploading ? 'Uploading...' : 'Upload CSV' }}
            </Button>
            <span v-if="selectedFile" class="body-sm text-text-secondary">{{ selectedFile.name }}</span>
          </div>
        </form>

        <p v-if="uploadError" class="mt-4 body-sm text-danger">{{ uploadError }}</p>

        <div v-if="importSummary" class="mt-5 p-4 rounded-sm border border-border-subtle bg-surface-raised">
          <h4 class="heading-sm text-text-primary mb-3">Latest import summary</h4>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-3 mb-4">
            <div>
              <p class="label text-text-muted">Imported</p>
              <p class="heading-sm text-text-primary">{{ importSummary.importedCount }}</p>
            </div>
            <div>
              <p class="label text-text-muted">Skipped</p>
              <p class="heading-sm text-text-primary">{{ importSummary.skippedCount }}</p>
            </div>
            <div>
              <p class="label text-text-muted">Event ID</p>
              <p class="body-sm text-text-secondary break-all">{{ importSummary.eventId }}</p>
            </div>
          </div>

          <div v-if="importSummary.skippedRows && importSummary.skippedRows.length > 0">
            <h5 class="label text-text-primary mb-2">Skipped rows</h5>
            <div class="space-y-2 max-h-48 overflow-auto pr-2">
              <div
                v-for="row in importSummary.skippedRows"
                :key="`${row.rowNumber}-${row.reason}`"
                class="text-body-sm p-3 rounded-sm border border-border-subtle bg-canvas"
              >
                <span class="font-semibold text-text-primary">Row {{ row.rowNumber }}:</span>
                <span class="text-text-secondary"> {{ row.reason }}</span>
              </div>
            </div>
          </div>
        </div>
      </Card>

      <Card v-if="showNewCellar" class="mb-8 bg-brand-primary-soft border-brand-primary">
        <h3 class="heading-sm text-text-primary mb-4">Create a New Cellar</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
          <input
            type="text"
            placeholder="Cellar name"
            class="px-4 py-3 rounded-sm border border-border-subtle bg-surface-raised text-text-primary focus:outline-none focus:ring-2 focus:ring-focus"
          >
          <input
            type="text"
            placeholder="Region or description"
            class="px-4 py-3 rounded-sm border border-border-subtle bg-surface-raised text-text-primary focus:outline-none focus:ring-2 focus:ring-focus"
          >
        </div>
        <div class="flex gap-3">
          <Button variant="primary" size="sm">Create</Button>
          <Button variant="secondary" size="sm" @click="showNewCellar = false">Cancel</Button>
        </div>
      </Card>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <RouterLink v-for="cellar in cellars" :key="cellar.id" :to="`/cellars/${cellar.id}`">
          <Card class="h-full cursor-pointer hover:shadow-md transition-shadow hover:border-brand-primary">
            <div class="flex justify-between items-start mb-4">
              <h2 class="heading-sm text-text-primary">{{ cellar.name }}</h2>
              <span class="label text-brand-secondary bg-brand-secondary-soft px-3 py-1 rounded-full">
                {{ cellar.bottleCount }}
              </span>
            </div>

            <p class="body-sm text-text-secondary mb-2">{{ cellar.region }}</p>
            <p class="body-sm text-text-muted mb-4">{{ cellar.description }}</p>

            <div class="flex justify-between items-center pt-4 border-t border-border-subtle">
              <span class="label text-text-muted">Created {{ cellar.createdAt }}</span>
              <span class="text-brand-primary">→</span>
            </div>
          </Card>
        </RouterLink>
      </div>

      <Card v-if="cellars.length === 0" class="text-center py-12 bg-brand-primary-soft border-brand-primary">
        <div class="text-6xl mb-4">🍾</div>
        <h3 class="heading-md text-text-primary mb-2">No cellars yet</h3>
        <p class="body-md text-text-secondary">Create your first cellar to start building your collection</p>
        <Button variant="primary" size="lg" class="mt-6">Create First Cellar</Button>
      </Card>
    </div>
  </div>
</template>
