import { apiClient, ApiError } from '../../lib/api/client';

export type RegisterInput = {
  email: string;
  password: string;
  displayName: string;
};

export type SessionUser = {
  id: string;
  email: string;
  displayName: string;
};

export type SessionResponse = {
  user: SessionUser;
  expiresAt: string;
};

export type CellarSummary = {
  cellarId: string;
  name: string;
  location?: string;
  description?: string;
  memberRole: 'OWNER' | 'CONTRIBUTOR' | 'VIEWER';
  bottleCount?: number;
};

export async function registerAccount(input: RegisterInput) {
  return apiClient.post<{ status: string; activationExpiresAt: string }>('/api/auth/register', input);
}

export async function activateAccount(token: string) {
  return apiClient.post<{ status: string }>('/api/auth/activate', { token });
}

export async function signIn(email: string, password: string) {
  return apiClient.post<SessionResponse>('/api/auth/sign-in', { email, password });
}

export async function signOut() {
  return apiClient.post<void>('/api/auth/sign-out', {});
}

export async function getCurrentSession() {
  return apiClient.get<SessionResponse>('/api/auth/session');
}

export async function getCellars() {
  return apiClient.get<{ items: CellarSummary[] }>('/api/cellars');
}

export { ApiError };
