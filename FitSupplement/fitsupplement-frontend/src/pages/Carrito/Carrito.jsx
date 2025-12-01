import React, { useContext, useState } from 'react';
import ImageWithFallback from '../../components/ImageWithFallback';
import producto1 from '../../assets/producto1.jpg';
import producto2 from '../../assets/producto2.webp';
import producto3 from '../../assets/producto3.jpg';
import producto4 from '../../assets/producto4.jpg';
import producto5 from '../../assets/producto5.webp';
import producto6 from '../../assets/producto6.webp';
import producto7 from '../../assets/producto7.jpg';
import { Link } from 'react-router-dom';
import { CartContext } from '../../context/CartContext';
import { ProductContext } from '../../context/ProductContext';
import ProductCard from '../../components/ProductCard';
import ModalConfirm from '../../components/ModalConfirm';
import { createOrder } from '../../services/cartService';
import { getProfile } from '../../services/authService';
import './Carrito.css';

const formatPrice = (price) => {
  return price.toLocaleString('es-CO', { style: 'currency', currency: 'COP', minimumFractionDigits: 0, maximumFractionDigits: 0 });
};

const Carrito = () => {
  const { 
    cart, 
    removeFromCart, 
    updateQuantity, 
    applyCoupon,
    removeCoupon,
    appliedCoupon,
    getSubtotal,
    getDiscount,
    getShipping,
    getTax,
    getTotal,
    clearCart
  } = useContext(CartContext);
  
  const { products, refreshProducts, updateStock } = useContext(ProductContext);
  
  // Seleccionar 3 productos recomendados (por ejemplo, los primeros 3, o aleatorios)
  const recommendedProducts = products.slice(0, 3);
  
  const [couponCode, setCouponCode] = useState('');
  const [couponMessage, setCouponMessage] = useState('');
  const [showModal, setShowModal] = useState(false);

  const ensureUserId = async () => {
    try {
      const currentUser = JSON.parse(localStorage.getItem('currentUser'));
      if (currentUser?.id) return currentUser.id;
      const res = await getProfile();
      const data = res?.data;
      if (data?.id) {
        try { localStorage.setItem('currentUser', JSON.stringify(data)); } catch {}
        return data.id;
      }
    } catch {}
    return null;
  };

  const handleConfirmPayment = async () => {
    try {
      const userId = await ensureUserId();
      if (!userId) throw new Error('Debe iniciar sesión antes de pagar');

      // Crear orden en backend con items -> el backend valida stock, calcula IVA, actualiza stock, y guarda la orden
      const payload = {
        userId,
        items: cart.map(item => ({ productId: item.id, quantity: item.quantity })),
        shipping: getShipping(),
        discount: getDiscount()
      };

      const { data } = await createOrder(payload);
      // Actualizar stock local inmediatamente para reflejar el cambio sin esperar red de productos
      try {
        cart.forEach(item => {
          const prod = products.find(p => p.id === item.id);
          const newStock = Math.max(0, (prod?.stock ?? 0) - item.quantity);
          updateStock(item.id, newStock);
        });
      } catch {}
      clearCart();
      setShowModal(false);
      // Sincronizar con backend
      try { refreshProducts(); } catch {}
      try { window.dispatchEvent(new Event('purchaseCompleted')); } catch {}
      try { window.dispatchEvent(new Event('productsChanged')); } catch {}
    } catch (err) {
      const msg = err?.response?.data?.message || err?.message || 'Error al procesar pago';
      alert(msg);
      setShowModal(false);
    }
  };

  const handleApplyCoupon = () => {
    if (applyCoupon(couponCode)) {
      setCouponMessage('¡Cupón aplicado!');
    } else {
      setCouponMessage('Código inválido');
    }
    setCouponCode('');
  };

  const handleRemoveCoupon = () => {
    removeCoupon();
    setCouponMessage('Cupón removido');
  };

  return (
    <>
    <div className="cart-container">
      <h1>Tu Carrito de Compras</h1>
      {cart.length === 0 ? (
        <div className="empty-cart">
          <div className="empty-cart-icon"></div>
          <h2>Tu carrito está vacío</h2>
          <p>¡Agrega algunos productos increíbles!</p>
        </div>
      ) : (
        <>
          <div className="cart-items">
            {cart.map((item) => (
              <div key={item.id} className="cart-item">
                <ImageWithFallback
                  src={item.image}
                  alt={item.name}
                  className="item-image"
                  style={{ width: 100, height: 100, objectFit: 'cover', borderRadius: 4 }}
                  fallback={() => {
                    // Prefer product-local asset by id when available
                    const byId = {
                      1: producto1,
                      2: producto2,
                      3: producto3,
                      4: producto4,
                      5: producto5,
                      6: producto6,
                      7: producto7,
                    };
                    return byId[Number(item.id)] || '/images/product-placeholder.svg';
                  }}
                />
                <div className="item-details">
                  <h3>{item.name}</h3>
                  <p>Descripción: {item.description}</p>
                  <p>Categoría: {item.category}</p>
                  <div className="item-rating">
                    <span>{item.rating}</span>
                    <span>({item.reviews} reseñas)</span>
                  </div>
                  <p>Precio: {formatPrice(item.price)}</p>
                  <div className="quantity-control">
                    <button onClick={() => updateQuantity(item.id, item.quantity - 1)} disabled={item.quantity <= 1}>-</button>
                    <span>{item.quantity}</span>
                    <button onClick={() => updateQuantity(item.id, item.quantity + 1)}>+</button>
                  </div>
                  <p>Subtotal: {formatPrice(item.price * item.quantity)}</p>
                </div>
                <button className="remove-btn" onClick={() => removeFromCart(item.id)}>Eliminar</button>
              </div>
            ))}
          </div>
          <div className="cart-summary">
              <h2>Resumen</h2>
              <p>Subtotal: <strong>{formatPrice(getSubtotal())}</strong></p>
              <p>Descuento: <strong>-{formatPrice(getDiscount())}</strong></p>
              <p>Envío: <strong>{formatPrice(getShipping())}</strong></p>
              <p>IVA (19%): <strong>{formatPrice(getTax())}</strong></p>
              <p className="total">Total: <strong>{formatPrice(getTotal())}</strong></p>
              <div className="coupon-section">
                <input 
                  type="text" 
                  value={couponCode} 
                  onChange={(e) => setCouponCode(e.target.value)}
                  placeholder="Código de cupón"
                />
                <button onClick={appliedCoupon ? handleRemoveCoupon : handleApplyCoupon}>
                  {appliedCoupon ? 'Remover' : 'Aplicar'}
                </button>
                <p>{couponMessage}</p>
              </div>
              <button className="checkout-btn" onClick={() => setShowModal(true)}>Proceder al Pago</button>
            </div>
          <div className="recommended-products">
            <h2>Productos recomendados</h2>
            <div className="recommended-grid">
              {recommendedProducts.map((product) => (
                <ProductCard key={product.id} {...product} />
              ))}
            </div>
          </div>
        </>
      )}
    </div>
    <ModalConfirm
      isOpen={showModal}
      onClose={() => setShowModal(false)}
      onConfirm={handleConfirmPayment}
      message="Pago confirmado"
      showCancel={false}
    />
    </>
  );
};

export default Carrito;
