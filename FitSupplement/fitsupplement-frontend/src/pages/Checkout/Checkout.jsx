import React, { useState } from 'react';
import Header from '../../components/Layout/Header';
import Footer from '../../components/Layout/Footer';
import { useCart } from '../../context/CartContext';
import { useAuth } from '../../context/AuthContext';
import { createOrder } from '../../services/cartService';
import ModalConfirm from '../../components/ModalConfirm';
import './Checkout.css';

const Checkout = () => {
  const { clearCart, cart, getSubtotal, getDiscount, getShipping, getTax, getTotal } = useCart();
  const [showModal, setShowModal] = useState(false);
  const [processing, setProcessing] = useState(false);
  const [orderResult, setOrderResult] = useState(null);
  const { user } = useAuth();

  return (
    <>
      <Header />
      <main className="main">
        <section className="checkout-section">
          <div className="container" style={{ textAlign: 'center', padding: '2rem' }}>
            <h1>Checkout</h1>
            <p>Resumen de tu compra</p>
            <div style={{margin: '1rem 0'}}>
              <div>Subtotal: ${getSubtotal().toFixed(2)}</div>
              <div>Descuento: ${getDiscount().toFixed(2)}</div>
              <div>Envio: ${getShipping().toFixed(2)}</div>
              <div>IVA (19%): ${getTax().toFixed(2)}</div>
              <div style={{fontWeight: 'bold'}}>Total: ${getTotal().toFixed(2)}</div>
            </div>
            <button className="btn btn-primary" onClick={() => setShowModal(true)}>Proceder al Pago</button>
          </div>
        </section>
      </main>
      <Footer />

      <ModalConfirm
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        onConfirm={async () => {
          // Create an order on backend, include items and totals; backend will validate stock and compute tax
          if (!user?.id) {
            alert('Debes iniciar sesión para realizar el pago');
            setShowModal(false);
            return;
          }
          setProcessing(true);
          try {
            const payload = {
              userId: user.id,
              items: cart.map(i => ({ productId: i.id, quantity: i.quantity })),
              shipping: getShipping(),
              discount: getDiscount()
            };
            const { data } = await createOrder(payload);
            setOrderResult(data);
            clearCart();
            setShowModal(false);
            alert('Pago y orden creada correctamente. ID de orden: ' + data.id);
            try { window.dispatchEvent(new Event('purchaseCompleted')); } catch {}
            try { window.dispatchEvent(new Event('productsChanged')); } catch {}
          } catch (err) {
            const msg = err?.response?.data?.message || err?.message || 'Error al crear la orden';
            alert(msg);
          } finally {
            setProcessing(false);
          }
        }}
        message={processing ? 'Procesando pago...' : 'Confirmar pago y generar orden?'}
        showCancel={false}
      />

      {orderResult && (
        <div style={{ textAlign: 'center', padding: '1rem' }}>
          <h3>Orden creada: {orderResult.id}</h3>
          <div style={{ marginTop: 8 }}>
            <div>Subtotal: ${Number(orderResult.subtotal || 0).toFixed(2)}</div>
            <div>IVA (19%): ${Number(orderResult.tax || 0).toFixed(2)}</div>
            <div style={{ fontWeight: 'bold' }}>Total pagado: ${Number(orderResult.total || 0).toFixed(2)}</div>
          </div>
        </div>
      )}
    </>
  );
};

export default Checkout;