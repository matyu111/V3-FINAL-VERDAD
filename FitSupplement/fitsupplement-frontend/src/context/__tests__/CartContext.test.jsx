import React from 'react';
import { renderHook, act } from '@testing-library/react';
import { CartProvider, useCart } from '../CartContext';
import { ProductContext } from '../ProductContext';

const wrapperWithProducts = (products = []) => ({ children }) => (
  <ProductContext.Provider value={{ products }}>
    <CartProvider>{children}</CartProvider>
  </ProductContext.Provider>
);

describe('CartContext utilities', () => {
  afterEach(() => {
    localStorage.clear();
  });

  test('applyCoupon and getDiscount behave correctly', () => {
    const products = [{ id: 1, stock: 10, price: 10000 }];
    const { result } = renderHook(() => useCart(), { wrapper: wrapperWithProducts(products) });

    act(() => {
      const ok = result.current.applyCoupon('BIENVENIDO20');
      expect(ok).toBe(true);
    });

    act(() => {
      result.current.addToCart({ id: 1, name: 'P', price: 10000 });
    });

    // subtotal 10000, discount 20% = 2000
    expect(result.current.getSubtotal()).toBe(10000);
    expect(result.current.getDiscount()).toBe(2000);
  });

  test('updateQuantity clamps to available stock', () => {
    const products = [{ id: 2, stock: 3, price: 5000 }];
    const { result } = renderHook(() => useCart(), { wrapper: wrapperWithProducts(products) });

    act(() => {
      result.current.addToCart({ id: 2, name: 'Q', price: 5000 });
    });

    act(() => {
      result.current.updateQuantity(2, 10);
    });

    // quantity should be clamped to 3
    const subtotal = result.current.getSubtotal();
    expect(subtotal).toBe(3 * 5000);
  });

  test('clearCart empties storage and cart', () => {
    const products = [{ id: 3, stock: 2, price: 2000 }];
    const { result } = renderHook(() => useCart(), { wrapper: wrapperWithProducts(products) });

    act(() => {
      result.current.addToCart({ id: 3, name: 'Z', price: 2000 });
    });

    expect(result.current.getSubtotal()).toBe(2000);

    act(() => {
      result.current.clearCart();
    });

    expect(result.current.getSubtotal()).toBe(0);
  });
});
