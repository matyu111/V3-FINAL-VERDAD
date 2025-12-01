import React, { useState, useEffect, useContext } from 'react';
import { useLocation } from 'react-router-dom';
import './Productos.css';
import ProductCard from '../../components/ProductCard';
import { ProductContext } from '../../context/ProductContext';

const Productos = () => {
  const { products: contextProducts } = useContext(ProductContext);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [selectedPrice, setSelectedPrice] = useState('');
  const location = useLocation();

  // Leer filtros desde la URL (?categoria=...&precio=...)
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const categoriaParam = params.get('categoria');
    const precioParam = params.get('precio');
    if (categoriaParam) setSelectedCategory(categoriaParam);
    if (precioParam) setSelectedPrice(precioParam);
  }, [location.search]);

  useEffect(() => {
    let tempProducts = contextProducts;

    // Ocultar productos inactivos
    tempProducts = tempProducts.filter(p => p.activo !== false);

    // Ocultar producto placeholder del sistema
    tempProducts = tempProducts.filter(p => {
      const isPlaceholderCategory = (p.category || '').toUpperCase() === 'PLACEHOLDER';
      const isPlaceholderName = (p.name || '').toLowerCase() === 'producto eliminado';
      return !(isPlaceholderCategory || isPlaceholderName);
    });

    if (selectedCategory) {
      tempProducts = tempProducts.filter(p => p.category === selectedCategory);
    }

    if (selectedPrice) {
      // Soporta rangos "min-max" y formato "30000+" para precio mínimo
      const parseRange = (value) => {
        if (!value) return [0, Infinity];
        if (value.endsWith('+')) {
          const min = parseInt(value) || 0;
          return [min, Infinity];
        }
        const [minStr, maxStr] = value.split('-');
        const min = parseInt(minStr);
        const max = parseInt(maxStr);
        return [isNaN(min) ? 0 : min, isNaN(max) ? Infinity : max];
      };

      const [min, max] = parseRange(selectedPrice);
      tempProducts = tempProducts.filter(p => p.price >= min && p.price <= max);
    }

    if (searchTerm) {
      tempProducts = tempProducts.filter(p => 
        (p.name || '').toLowerCase().includes(searchTerm.toLowerCase()) || 
        (p.description || '').toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    setFilteredProducts(tempProducts);
  }, [searchTerm, selectedCategory, selectedPrice, contextProducts]);

  return (
    <main className="main">
      <section className="products-header">
        <div className="container">
          <h1>Nuestros Productos</h1>
          <div className="search-filters">
            <div className="search-box">
              <input 
                type="text" 
                id="searchInput" 
                placeholder="Buscar productos..." 
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
              <button type="button">🔍</button>
            </div>
            <div className="filters">
              <select 
                id="categoryFilter" 
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value)}
              >
                <option value="">Todas las categorías</option>
                <option value="proteinas">Proteínas</option>
                <option value="creatina">Creatina</option>
                <option value="pre-entreno">Pre-entreno</option>
                <option value="vitaminas">Vitaminas</option>
                <option value="aminoacidos">Aminoácidos</option>
                <option value="quemadores">Quemadores de grasa</option>
              </select>
              <select 
                id="priceFilter"
                value={selectedPrice}
                onChange={(e) => setSelectedPrice(e.target.value)}
              >
                <option value="">Todos los precios</option>
                <option value="0-20000">$0 - $20.000</option>
                <option value="20000-30000">$20.000 - $30.000</option>
                <option value="30000+">$30.000+</option>
              </select>
            </div>
          </div>
        </div>
      </section>

      <section className="products-section">
        <div className="container">
          <div className="products-grid" id="productsGrid">
            {filteredProducts.map((product, index) => (
              <ProductCard key={index} {...product} />
            ))}
          </div>
          <div className="results-counter" style={{ textAlign: 'center', margin: '1rem 0', color: '#666', fontSize: '0.9rem' }}>
            Mostrando {filteredProducts.length} de {contextProducts.length} productos
          </div>
          {filteredProducts.length === 0 && (
            <div className="no-results" id="noResults">
              <h3>No se encontraron productos</h3>
              <p>Intenta ajustar los filtros de búsqueda</p>
            </div>
          )}
        </div>
      </section>
    </main>
  );
};

export default Productos;