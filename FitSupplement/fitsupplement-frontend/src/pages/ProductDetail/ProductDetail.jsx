import React, { useState, useContext, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ProductContext } from '../../context/ProductContext';
import { CartContext } from '../../context/CartContext';
import ModalConfirm from '../../components/ModalConfirm';
import './ProductDetail.css';
// Assets locales enumerados 1-7
import producto1 from '../../assets/producto1.jpg';
import producto2 from '../../assets/producto2.webp';
import producto3 from '../../assets/producto3.jpg';
import producto4 from '../../assets/producto4.jpg';
import producto5 from '../../assets/producto5.webp';
import producto6 from '../../assets/producto6.webp';
import producto7 from '../../assets/producto7.jpg';

const ProductDetail = () => {
  const { id } = useParams();
  const { products } = useContext(ProductContext);
  const { addToCart } = useContext(CartContext);
  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [showModal, setShowModal] = useState(false);
  const [relatedProducts, setRelatedProducts] = useState([]);

  const getFallbackSrc = (cat, productId) => {
    const byId = {
      1: producto1,
      2: producto2,
      3: producto3,
      4: producto4,
      5: producto5,
      6: producto6,
      7: producto7,
    };
    if (byId[Number(productId)]) return byId[Number(productId)];
    const byCategory = {
      'proteinas': producto1,
      'creatina': producto3,
      'pre-entreno': producto5,
      'vitaminas': producto7,
      'aminoacidos': producto6,
      'quemadores': producto4,
    };
    return byCategory[String(cat || '').toLowerCase()] || producto3;
  };

  useEffect(() => {
    const foundProduct = products.find(p => p.id === parseInt(id));
    if (foundProduct) {
      setProduct(foundProduct);
      // Seleccionar relacionados: primero de la misma categoría
      let related = products.filter(p => p.category === foundProduct.category && p.id !== foundProduct.id);
      // Si hay menos de 4, completar con otros productos aleatorios
      if (related.length < 4) {
        const others = products.filter(p => p.id !== foundProduct.id && p.category !== foundProduct.category);
        // Mezclar y tomar los que faltan
        const shuffled = others.sort(() => 0.5 - Math.random());
        related = [...related, ...shuffled.slice(0, 4 - related.length)];
      }
      setRelatedProducts(related.slice(0, 4));
    }
  }, [id, products]);

  const handleAddToCart = () => {
    if ((product?.stock ?? 0) <= 0) return;
    setShowModal(true);
  };

  const confirmAdd = () => {
    addToCart({ ...product, quantity });
    setShowModal(false);
  };

  const handleQuantityChange = (delta) => {
    setQuantity(prev => {
      const next = Math.max(1, prev + delta);
      const max = product?.stock ?? 1;
      return Math.min(max, next);
    });
  };

  if (!product) return <div>Producto no encontrado</div>;

  return (
    <div className="product-detail">
      <div className="product-main">
        <div className="product-image-container">
          <img
            key={product.id}
            src={product.image || getFallbackSrc(product.category, product.id)}
            alt={product.name}
            className="product-detail-img"
            onError={(e) => {
              e.currentTarget.src = getFallbackSrc(product.category, product.id);
              e.currentTarget.onerror = null;
            }}
          />
        </div>
        <div className="product-info">
          <h1>{product.name}</h1>
          <p className="product-category">Categoría: {product.category}</p>
          <p className="product-description">{product.description}</p>
          <p className="product-price">${product.price.toLocaleString('es-CO')}</p>
          <p className="product-stock">{(product.stock ?? 0) > 0 ? `Stock disponible: ${product.stock}` : 'Sin stock'}</p>
          {(product.stock ?? 0) > 0 && (product.stock ?? 0) <= 5 && (
            <p className="low-stock-badge">Últimas unidades: quedan {product.stock}</p>
          )}
          <div className="quantity-selector">
            <button onClick={() => handleQuantityChange(-1)}>-</button>
            <span>{quantity}</span>
            <button onClick={() => handleQuantityChange(1)}>+</button>
          </div>
          <button onClick={handleAddToCart} className="add-to-cart" disabled={(product.stock ?? 0) <= 0}>
            {(product.stock ?? 0) <= 0 ? 'Sin stock' : 'Agregar al carrito'}
          </button>
        </div>
      </div>
      <div className="related-products">
        <h2>Productos relacionados</h2>
        <div className="related-grid">
          {relatedProducts.map(rel => (
            <Link key={rel.id} to={`/productos/${rel.id}`} className="related-card" style={{ textDecoration: 'none', color: 'inherit' }}>
              <img
                key={rel.id}
                src={rel.image || getFallbackSrc(rel.category, rel.id)}
                alt={rel.name}
                onError={(e) => {
                  e.currentTarget.src = getFallbackSrc(rel.category, rel.id);
                  e.currentTarget.onerror = null;
                }}
              />
              <h3>{rel.name}</h3>
              <p className="related-price">${rel.price.toLocaleString('es-CO')}</p>
            </Link>
          ))}
        </div>
      </div>
      {showModal && (
        <ModalConfirm
          isOpen={showModal}
          message={`¿Agregar ${quantity} unidad(es) de ${product.name} al carrito?`}
          onConfirm={confirmAdd}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
};

export default ProductDetail;