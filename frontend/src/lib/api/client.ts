export class ApiError extends Error {
  code: string;

  constructor(message: string, code = 'API_ERROR') {
    super(message);
    this.code = code;
  }
}

function resolveBaseUrl() {
  return import.meta.env.VITE_API_BASE_URL ?? '';
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${resolveBaseUrl()}${path}`, {
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
      ...(init?.headers ?? {}),
    },
    ...init,
  });

  if (!response.ok) {
    const payload = await response.json().catch(() => ({ message: 'Request failed', code: 'REQUEST_FAILED' }));
    throw new ApiError(payload.message ?? 'Request failed', payload.code ?? 'REQUEST_FAILED');
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export const apiClient = {
  get<T>(path: string) {
    return request<T>(path);
  },
  post<T>(path: string, body: unknown) {
    return request<T>(path, {
      method: 'POST',
      body: JSON.stringify(body),
    });
  },
};
