import { createContext, useState, useEffect } from 'react';
import { getAllProducts } from '../services/productService';

export const ProductContext = createContext();

export const ProductProvider = ({ children }) => {
  const [products, setProducts] = useState([]);

  const loadProducts = async () => {
    try {
      const { data } = await getAllProducts();
      const mapped = (Array.isArray(data) ? data : []).map(p => ({
        id: p.id,
        name: p.nombre,
        description: p.descripcion,
        price: Number(p.precio) || 0,
        stock: Number(p.stock) || 0,
        category: p.categoria,
        image: p.imagen || 'https://images.unsplash.com/photo-1514996937319-344454492b37?w=800&q=80&auto=format&fit=crop',
        activo: Boolean(p.activo),
      }));
      const activos = mapped.filter(p => p.activo);
      setProducts(activos);
      try {
        localStorage.setItem('products', JSON.stringify(activos));
      } catch {}
    } catch {
      // Fallback: usar cache local si existe
      try {
        const stored = localStorage.getItem('products');
        if (stored) {
          setProducts(JSON.parse(stored));
        }
      } catch {}
    }
  };

  useEffect(() => {
    loadProducts();
    const handler = () => {
      loadProducts();
    };
    window.addEventListener('productsChanged', handler);
    return () => window.removeEventListener('productsChanged', handler);
  }, []);

  const updateStock = (productId, newStock) => {
    setProducts(prevProducts => {
      const updated = prevProducts.map(p => 
        p.id === productId ? { ...p, stock: newStock } : p
      );
      localStorage.setItem('products', JSON.stringify(updated));
      return updated;
    });
  };

  const refreshProducts = () => loadProducts();

  return (
    <ProductContext.Provider value={{ products, updateStock, refreshProducts }}>
      {children}
    </ProductContext.Provider>
  );
};