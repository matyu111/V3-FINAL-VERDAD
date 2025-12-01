import React from 'react';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

// Mock cart hook so addToCart is a spy
const mockAdd = vi.fn();
vi.mock('../../context/CartContext', () => ({
  useCart: () => ({ addToCart: mockAdd })
}));

// Mock image component and router navigate before importing the tested module
vi.mock('../ImageWithFallback', () => ({ default: (props) => <img alt={props.alt} src={props.src || '/images/product-placeholder.svg'} /> }));
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return { ...actual, useNavigate: () => () => {} };
});

import ProductCard from '../ProductCard';

describe('ProductCard', () => {
  afterEach(() => {
    cleanup();
    vi.clearAllMocks();
  });

  it('uses fallback image when image prop is missing', () => {
    render(
      <MemoryRouter>
        <ProductCard id={123} category="proteinas" price={1000} name="Test" description="desc" stock={10} />
      </MemoryRouter>
    );

    const img = screen.getByRole('img');
    expect(img).toBeTruthy();
    const src = img.getAttribute('src');
    expect(typeof src).toBe('string');
    expect(src.length).toBeGreaterThan(0);
  });

  it('calls addToCart when Add to Cart button clicked and in stock', () => {
    const { getByRole } = render(
      <MemoryRouter>
        <ProductCard id={1} category="proteinas" price={2000} name="Whey" description="X" stock={5} />
      </MemoryRouter>
    );

    const addBtn = getByRole('button', { name: /Agregar al Carrito/i });
    fireEvent.click(addBtn);

    const confirmBtn = getByRole('button', { name: /Confirmar/i });
    fireEvent.click(confirmBtn);

    expect(mockAdd).toHaveBeenCalled();
  });
});
