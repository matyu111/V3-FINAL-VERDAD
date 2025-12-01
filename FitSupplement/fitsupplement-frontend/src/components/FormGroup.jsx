import React from 'react';

const FormGroup = ({ label, id, children, errorId }) => {
  return (
    <div className="form-group">
      <label htmlFor={id}>{label}</label>
      {children}
      {errorId && <span className="error-message" id={errorId}></span>}
    </div>
  );
};

export default FormGroup;