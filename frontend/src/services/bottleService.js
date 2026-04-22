import { apiFetch } from './apiClient'

export async function listDefaultCellarBottles({ page = 0, size = 50 } = {}) {
  const query = new URLSearchParams({
    page: String(page),
    size: String(size),
  })

  return apiFetch(`/api/cellars/default/bottles?${query.toString()}`)
}
