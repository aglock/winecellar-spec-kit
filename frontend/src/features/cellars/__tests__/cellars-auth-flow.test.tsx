import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { CellarsPage } from '../CellarsPage';
import * as authApi from '../../auth/auth-api';

vi.mock('../../auth/auth-api');

describe('CellarsPage', () => {
  it('renders empty state when no cellars are returned', async () => {
    vi.mocked(authApi.getCellars).mockResolvedValue({ items: [] });

    render(
      <MemoryRouter>
        <CellarsPage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/no accessible cellars yet/i)).toBeInTheDocument();
    });
  });
});
