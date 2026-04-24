import { apiFetch } from './apiClient'

export async function importWineBottlesCsv(file) {
  if (!file) {
    throw new Error('Select a CSV file before uploading')
  }

  const formData = new FormData()
  formData.append('file', file)

  return apiFetch('/api/imports/wine-bottles', {
    method: 'POST',
    body: formData,
  })
}
