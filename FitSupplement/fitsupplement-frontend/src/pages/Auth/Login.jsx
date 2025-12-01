import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../styles/Login.css'; // Asumiremos que crearemos este archivo
import { useAuth } from '../../context/AuthContext.jsx';
import ModalConfirm from '../../components/ModalConfirm.jsx';

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!email || !password) {
      setError('Por favor completa todos los campos');
      return;
    }

    // Validar dominio permitido
    const allowedDomains = ['@duoc.cl', '@gmail.com', '@profesor.duoc.cl', '@profesor.com'];
    if (!allowedDomains.some(d => email.endsWith(d))) {
      const msg = 'El correo debe ser @duoc.cl, @gmail.com, @profesor.duoc.cl o @profesor.com';
      setError(msg);
      setErrorMessage(msg);
      setShowErrorModal(true);
      return;
    }

    const res = await login(email, password);
    if (res.ok) {
      setShowModal(true);
    } else {
      setError(res.message);
      setErrorMessage(res.message);
      setShowErrorModal(true);
    }
  };

  return (
    <div className="login-section">
      <div className="container">
        <div className="login-container">
          <div className="login-form-container">
            <div className="login-header">
              <h1>¡Bienvenido de vuelta!</h1>
              <p className="login-subtitle">Inicia sesión para acceder a tu cuenta</p>
            </div>

            <form className="login-form" onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="email">Correo electrónico *</label>
                <input
                  type="email"
                  id="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="password">Contraseña *</label>
                <input
                  type="password"
                  id="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>

              {error && <p className="error-message">{error}</p>}

              <button type="submit" className="btn btn-login">
                Iniciar Sesión
              </button>
            </form>

            <div className="login-footer">
              <p>¿No tienes cuenta? <Link to="/registro">Regístrate aquí</Link></p>
            </div>
          </div>

          <div className="login-benefits">
            <h2>¿Por qué iniciar sesión?</h2>
            <div className="benefits-list">
              {/* Lista de beneficios como en el HTML original */}
              <div className="benefit-item">🛒 Carrito guardado</div>
              <div className="benefit-item">📃 Historial de pedidos</div>
              <div className="benefit-item">⭐ Puntos acumulados</div>
              <div className="benefit-item">🎯 Recomendaciones</div>
              <div className="benefit-item">🚚 Seguimiento de envíos</div>
              <div className="benefit-item">💬 Soporte prioritario</div>
            </div>
          </div>
        </div>
      </div>
      {showModal && (
        <ModalConfirm
          isOpen={showModal}
          message="Inicio de sesión correctamente"
          onConfirm={() => { setShowModal(false); navigate('/'); }}
          onClose={() => setShowModal(false)}
          showCancel={false}
        />
      )}
      {showErrorModal && (
        <ModalConfirm
          isOpen={showErrorModal}
          message={errorMessage || 'Correo o contraseña inválidos'}
          onConfirm={() => setShowErrorModal(false)}
          onClose={() => setShowErrorModal(false)}
          showCancel={false}
        />
      )}
    </div>
  );
};

export default Login;