import React from 'react';
import './ProductCard.css';
import { useCart } from '../context/CartContext';
import { useNavigate } from 'react-router-dom';
import ModalConfirm from './ModalConfirm';
// Assets locales enumerados 1-7 proporcionados por el usuario
import producto1 from '../assets/producto1.jpg';
import producto2 from '../assets/producto2.webp';
import producto3 from '../assets/producto3.jpg';
import producto4 from '../assets/producto4.jpg';
import producto5 from '../assets/producto5.webp';
import producto6 from '../assets/producto6.webp';
import producto7 from '../assets/producto7.jpg';
import ImageWithFallback from './ImageWithFallback';

const ProductCard = ({ id, category, price, image, name, description, rating, reviews, stock }) => {
  const { addToCart } = useCart();
  const navigate = useNavigate();

  const [showConfirm, setShowConfirm] = React.useState(false);
  const [showSuccess, setShowSuccess] = React.useState(false);
  const isOutOfStock = (stock ?? 0) <= 0;

  const getFallbackSrc = (cat, productId) => {
    // Primero intenta por ID (antiguos 1-7)
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
    // Luego por categoría (fallback genérico)
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

  const handleAddToCart = () => {
    if (isOutOfStock) return;
    setShowConfirm(true);
  };

  return (
    <>
        <article className="product-card" data-category={category} data-price={price} onClick={() => navigate(`/productos/${id}`)}>
          <div className="product-image">
            <ImageWithFallback
              src={image || getFallbackSrc(category, id)}
              alt={name}
              style={{ height: '280px', width: 'auto', maxWidth: '100%', objectFit: 'contain', display: 'block', margin: '0 auto' }}
            />
          </div>
          <div className="product-info">
            <h3>{name}</h3>
            {/* Ocultamos rating/reseñas en listado para alineación visual */}
          <div className="product-price">
            <span className="price">${(price ?? 0).toLocaleString('es-CO')}</span>
          </div>
            <div className="product-stock">{isOutOfStock ? 'Sin stock' : `Stock: ${stock}`}</div>
            <button
              onClick={(e) => { e.preventDefault(); e.stopPropagation(); handleAddToCart(); }}
              className="btn btn-primary add-to-cart"
              data-product={id}
              type="button"
              disabled={isOutOfStock}
            >
              {isOutOfStock ? 'Sin stock' : 'Agregar al Carrito'}
            </button>
          </div>
        </article>

      <ModalConfirm
        isOpen={showConfirm}
        onClose={() => setShowConfirm(false)}
        onConfirm={() => {
          addToCart({ id, name, price: Number(price ?? 0), image, description, rating, reviews, category });
          setShowConfirm(false);
          setShowSuccess(true);
        }}
        message="¿Deseas agregar este producto al carrito?"
      />

      <ModalConfirm
        isOpen={showSuccess}
        onClose={() => setShowSuccess(false)}
        onConfirm={() => setShowSuccess(false)}
        message="Producto agregado al carrito exitosamente!"
        showCancel={false}
      />
    </>

  );

}

export default ProductCard;