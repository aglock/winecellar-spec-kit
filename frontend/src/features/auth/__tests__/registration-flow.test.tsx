import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { RegisterPage } from '../RegisterPage';
import { ActivatePage } from '../ActivatePage';
import * as authApi from '../auth-api';

vi.mock('../auth-api');

describe('registration flow', () => {
  it('submits registration and shows activation status', async () => {
    const user = userEvent.setup();
    vi.mocked(authApi.registerAccount).mockResolvedValue({
      status: 'PENDING_ACTIVATION',
      activationExpiresAt: '2026-03-17T12:00:00Z',
    });

    render(
      <MemoryRouter>
        <RegisterPage />
      </MemoryRouter>
    );

    await user.type(screen.getByLabelText(/display name/i), 'Collector');
    await user.type(screen.getByLabelText(/^email$/i), 'collector@example.com');
    await user.type(screen.getByLabelText(/password/i), 'very-secure-password');
    await user.click(screen.getByRole('button', { name: /^register$/i }));

    expect(await screen.findByText(/registration accepted/i)).toBeInTheDocument();
  });

  it('shows activation success when token resolves', async () => {
    vi.mocked(authApi.activateAccount).mockResolvedValue({ status: 'ACTIVE' });

    render(
      <MemoryRouter initialEntries={['/activate?token=test-token']}>
        <ActivatePage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/account activated/i)).toBeInTheDocument();
    });
  });
});
