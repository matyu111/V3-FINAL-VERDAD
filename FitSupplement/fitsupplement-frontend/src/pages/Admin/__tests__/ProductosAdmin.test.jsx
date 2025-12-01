import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, vi, beforeEach, expect } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

// Mocks para servicios que hace llamadas HTTP
vi.mock('../../../services/adminService', () => ({
  getAllProductsAdmin: vi.fn(() => Promise.resolve({ data: [] })),
  toggleProduct: vi.fn(),
}));
vi.mock('../../../services/productService', () => ({
  createProduct: vi.fn(() => Promise.resolve({ data: { id: 1, nombre: 'p', descripcion: '', precio: 0, stock: 0, categoria: '', imagen: '' } })),
  updateProduct: vi.fn(() => Promise.resolve({ data: { id: 1, nombre: 'p', descripcion: '', precio: 0, stock: 0, categoria: '', imagen: '' } })),
  deleteProduct: vi.fn(),
}));

import ProductosAdmin from '../ProductosAdmin';

describe('ProductosAdmin modal imagen URL validation', () => {
  beforeEach(() => {
    // renderizar el componente
    render(
      <MemoryRouter>
        <ProductosAdmin />
      </MemoryRouter>
    );
  });

  it('muestra mensaje de URL inválida y deshabilita Guardar cuando la imagen falla en cargar', async () => {
    // Abrir modal: botón 'Agregar Producto'
    const addBtn = await screen.findByRole('button', { name: /Agregar Producto/i });
    userEvent.click(addBtn);

    // Encontrar input de imagen
    const input = await screen.findByPlaceholderText(/https:\/\//i);
    // Escribir una URL (falsa) que fallará
    await userEvent.type(input, 'https://example.invalid/image.png');

    // El preview de imagen tiene alt "preview"
    const img = await screen.findByAltText('preview');
    // Disparar evento error en la imagen para simular carga fallida
    fireEvent.error(img);

    // Esperar a que aparezca el mensaje de URL inválida
    await waitFor(() => {
      // getByText lanzará si no existe, por lo que su presencia implica éxito
      expect(screen.getByText(/La URL no carga correctamente/i)).toBeTruthy();
    });

    // El botón Guardar debería estar deshabilitado
    const saveBtn = screen.getByRole('button', { name: /Guardar/i });
    expect(saveBtn.disabled).toBe(true);
  });
});
