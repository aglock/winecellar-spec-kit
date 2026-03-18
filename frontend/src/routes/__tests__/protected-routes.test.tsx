import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { App } from '../../app/App';
import * as authApi from '../../features/auth/auth-api';
import { resetAuthStoreForTests } from '../../lib/auth/auth-store';

vi.mock('../../features/auth/auth-api');

describe('protected routes', () => {
  beforeEach(() => {
    resetAuthStoreForTests();
    vi.clearAllMocks();
  });

  it('redirects to sign-in when the session bootstrap fails', async () => {
    vi.mocked(authApi.getCurrentSession).mockRejectedValue(new authApi.ApiError('Session missing'));

    render(
      <MemoryRouter initialEntries={['/cellars']}>
        <App />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByRole('heading', { name: /welcome back/i })).toBeInTheDocument();
    });
  });

  it('renders the cellar page when the session is active', async () => {
    vi.mocked(authApi.getCurrentSession).mockResolvedValue({
      user: { id: 'user-1', email: 'collector@example.com', displayName: 'Collector' },
      expiresAt: '2026-03-17T12:00:00Z',
    });
    vi.mocked(authApi.getCellars).mockResolvedValue({
      items: [
        {
          cellarId: 'cellar-1',
          name: 'Reserve',
          memberRole: 'OWNER',
          location: 'Bordeaux',
          description: 'Private cellar',
        },
      ],
    });

    render(
      <MemoryRouter initialEntries={['/cellars']}>
        <App />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/your cellar universe/i)).toBeInTheDocument();
      expect(screen.getByText('Reserve')).toBeInTheDocument();
    });
  });
});
