import React, { useEffect } from 'react';
import './ModalConfirm.css'; // styles for modal

const ModalConfirm = ({ isOpen, onClose, onConfirm, message, showCancel = true, confirmLabel = 'Confirmar', cancelLabel = 'Cancelar' }) => {
  useEffect(() => {
    // prevent body scroll while overlay open
    if (isOpen) document.body.style.overflow = 'hidden';
    return () => { document.body.style.overflow = ''; };
  }, [isOpen]);

  useEffect(() => {
    const onKey = (e) => { if (e.key === 'Escape') onClose && onClose(); };
    if (isOpen) window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <div className="modal-confirm-overlay" role="dialog" aria-modal="true" aria-label="Confirmar acción">
      <div className="modal-confirm-content">
        <button className="modal-close" aria-label="Cerrar" onClick={onClose}>✕</button>
        <div className="modal-message">{message}</div>
        <div className="modal-buttons">
          <button onClick={onConfirm} className="modal-confirm-btn">{confirmLabel}</button>
          {showCancel && (
            <button onClick={onClose} className="modal-cancel-btn">{cancelLabel}</button>
          )}
        </div>
      </div>
    </div>
  );
};

export default ModalConfirm;