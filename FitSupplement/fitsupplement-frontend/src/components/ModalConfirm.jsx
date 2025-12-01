import React from 'react';
import './ModalConfirm.css'; // Asumiendo que hay un CSS para estilizar el modal

const ModalConfirm = ({ isOpen, onClose, onConfirm, message, showCancel = true }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <p>{message}</p>
        <div className="modal-buttons">
          <button onClick={onConfirm} className="btn btn-primary">Confirmar</button>
          {showCancel && (
            <button onClick={onClose} className="btn btn-secondary">Cancelar</button>
          )}
        </div>
      </div>
    </div>
  );
};

export default ModalConfirm;