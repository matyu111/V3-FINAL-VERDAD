import React from 'react';

const PaymentMethod = ({ id, value, icon, text }) => {
  return (
    <div className="payment-method">
      <input type="radio" id={id} name="paymentMethod" value={value} required />
      <label htmlFor={id} className="payment-label">
        <span className="payment-icon">{icon}</span>
        <span className="payment-text">{text}</span>
      </label>
    </div>
  );
};

export default PaymentMethod;