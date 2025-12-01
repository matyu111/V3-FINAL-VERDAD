import { createContext, useContext, useState, useEffect } from "react";
import { ProductContext } from './ProductContext';

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
  const [cart, setCart] = useState([]);
  const [appliedCoupon, setAppliedCoupon] = useState(null);
  const { products } = useContext(ProductContext);

  useEffect(() => {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      setCart(JSON.parse(storedCart));
    }
  }, []);

  const saveCartToStorage = (updatedCart) => {
    localStorage.setItem('cart', JSON.stringify(updatedCart));
    setCart(updatedCart);
    window.dispatchEvent(new Event('cartUpdated'));
  };

  const addToCart = (product) => {
    const quantityToAdd = product.quantity || 1;
    const productInStock = products.find(p => p.id === product.id);
    if (!productInStock) return;

    const existingItem = cart.find(item => item.id === product.id);
    const reserved = existingItem ? existingItem.quantity : 0;
    const available = (productInStock.stock ?? 0) - reserved;

    if (available <= 0) {
      console.log('Stock insuficiente');
      alert('No hay stock disponible para agregar más unidades.');
      return;
    }

    const addQty = Math.min(quantityToAdd, available);
    let updatedCart;
    if (existingItem) {
      updatedCart = cart.map(item =>
        item.id === product.id ? { ...item, quantity: item.quantity + addQty } : item
      );
    } else {
      updatedCart = [...cart, { ...product, quantity: addQty }];
    }
    saveCartToStorage(updatedCart);
  };

  const removeFromCart = (productId) => {
    const itemToRemove = cart.find(item => item.id === productId);
    if (itemToRemove) {
      const updatedCart = cart.filter(item => item.id !== productId);
      saveCartToStorage(updatedCart);
    }
  };

  const updateQuantity = (productId, newQuantity) => {
    const item = cart.find(item => item.id === productId);
    if (!item) return;
    const productInStock = products.find(p => p.id === productId);
    if (!productInStock) return;

    const maxAllowed = productInStock.stock ?? 0;
    const clamped = Math.max(1, Math.min(newQuantity, maxAllowed));
    const updatedCart = cart.map(i =>
      i.id === productId ? { ...i, quantity: clamped } : i
    );
    saveCartToStorage(updatedCart);
    if (newQuantity > maxAllowed) {
      alert(`Solo hay ${maxAllowed} unidad(es) disponibles de este producto.`);
    }
  };

  const clearCart = () => {
    localStorage.removeItem("cart");
    setCart([]);
    window.dispatchEvent(new Event("cartUpdated"));
  };

  const applyCoupon = (code) => {
    const coupons = {
      'BIENVENIDO20': { type: 'percentage', value: 20 },
      'ENVIOGRATIS': { type: 'fixed', value: 5000 },
      'FITNESS15': { type: 'percentage', value: 15 },
      'NUEVO10': { type: 'percentage', value: 10 }
    };
    const coupon = coupons[code.toUpperCase()];
    if (coupon) {
      setAppliedCoupon({ code, ...coupon });
      return true;
    }
    return false;
  };

  const removeCoupon = () => setAppliedCoupon(null);

  const getSubtotal = () => cart.reduce((sum, p) => sum + (p.price || 0) * (p.quantity || 1), 0);

  const getDiscount = () => {
    if (!appliedCoupon) return 0;
    const subtotal = getSubtotal();
    return appliedCoupon.type === 'percentage' 
      ? subtotal * (appliedCoupon.value / 100) 
      : appliedCoupon.value;
  };

  const getShipping = () => {
    const subtotal = getSubtotal();
    if (subtotal >= 50000) return 0;
    if (subtotal >= 30000) return 3000;
    return 5000;
  };

  // IVA (19%) should be applied over the taxable base (subtotal - discount)
  const getTax = () => {
    const subtotal = getSubtotal();
    const discount = getDiscount();
    const taxable = Math.max(0, subtotal - discount);
    return Number((taxable * 0.19).toFixed(2));
  };

  const getTotal = () => {
    const subtotal = getSubtotal();
    const discount = getDiscount();
    const shipping = getShipping();
    const tax = getTax();
    return Number((subtotal - discount + tax + shipping).toFixed(2));
  };

  return (
    <CartContext.Provider value={{ 
      cart, 
      addToCart, 
      removeFromCart, 
      updateQuantity, 
      clearCart,
      applyCoupon,
      removeCoupon,
      appliedCoupon,
      getSubtotal,
      getDiscount,
      getShipping,
      getTotal,
      getTax
    }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => useContext(CartContext);
