import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { App } from '../../app/App';

describe('public routes', () => {
  it('lets a signed-out visitor navigate from landing to registration', async () => {
    const user = userEvent.setup();

    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    );

    await user.click(screen.getAllByRole('link', { name: /^register$/i })[1]);

    expect(await screen.findByRole('heading', { name: /open your cellar/i })).toBeInTheDocument();
  });
});
