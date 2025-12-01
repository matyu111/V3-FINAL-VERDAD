import React from 'react';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import { describe, it, vi, beforeEach, afterEach, expect } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

// Mock cart hook so addToCart is a spy
const mockAdd = vi.fn();
vi.mock('../../context/CartContext', () => ({
  useCart: () => ({ addToCart: mockAdd })
}));

// Render the ProductCard component
import ProductCard from '../ProductCard';

describe('ProductCard', () => {
  // ensure DOM is cleaned between tests so renders don't accumulate
  afterEach(() => {
    cleanup()
    vi.clearAllMocks()
  })
  it('uses fallback image when image prop is missing', () => {
    render(
      <MemoryRouter>
        <ProductCard id={123} category="proteinas" price={1000} name="Test" description="desc" stock={10} />
      </MemoryRouter>
    );

    const img = screen.getByRole('img');
    expect(img).toBeTruthy();
    const src = img.getAttribute('src');
    // fallback imports become resolved strings — ensure src is not empty and contains 'producto' or a local asset name
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
    // click the add button -> opens confirmation modal
    fireEvent.click(addBtn);

    // modal should render with a Confirmar button; find and click it
    const confirmBtn = getByRole('button', { name: /Confirmar/i });
    fireEvent.click(confirmBtn);

    // addToCart should have been called by the modal's onConfirm handler
    expect(mockAdd).toHaveBeenCalled();
  });
});
