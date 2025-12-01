import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import './Header.css';
import { useAuth } from '../../context/AuthContext.jsx';

const Header = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const { user, isAdmin, isAuthenticated, logout } = useAuth();
  const [cartCount, setCartCount] = useState(0);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    // Actualizar contador del carrito
    const updateCartCount = () => {
      try {
        const cart = JSON.parse(localStorage.getItem('cart')) || [];
        const total = cart.reduce((sum, item) => sum + (item.quantity || 0), 0);
        setCartCount(total);
      } catch (e) {
        setCartCount(0);
      }
    };

    updateCartCount();
    window.addEventListener('cartUpdated', updateCartCount);
    
    return () => window.removeEventListener('cartUpdated', updateCartCount);
  }, []);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const closeMenu = () => {
    setIsMenuOpen(false);
  };

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  const handleNavLinkClick = () => {
    if (window.innerWidth <= 768) {
      closeMenu();
    }
  };

  return (
    <header className="header">
      <div className="container">
        <div className="header-content">
          <div className="logo">
            <Link to="/" onClick={closeMenu}>
              <h1>NeuroVision</h1>
              <span>Suplementos para tu fitness</span>
            </Link>
          </div>
          
          <nav className={`nav ${isMenuOpen ? 'active' : ''}`}>
            <ul className="nav-list">
              <li>
                <Link 
                  to="/" 
                  className={`nav-link ${isActive('/')}`}
                  onClick={handleNavLinkClick}
                >
                  Inicio
                </Link>
              </li>
              <li>
                <Link 
                  to="/productos" 
                  className={`nav-link ${isActive('/productos')}`}
                  onClick={handleNavLinkClick}
                >
                  Productos
                </Link>
              </li>
              <li>
                <Link 
                  to="/categorias" 
                  className={`nav-link ${isActive('/categorias')}`}
                  onClick={handleNavLinkClick}
                >
                  Categorías
                </Link>
              </li>
              <li>
                <Link 
                  to="/blog" 
                  className={`nav-link ${isActive('/blog')}`}
                  onClick={handleNavLinkClick}
                >
                  Blog
                </Link>
              </li>
              <li>
                <Link 
                  to="/contacto" 
                  className={`nav-link ${isActive('/contacto')}`}
                  onClick={handleNavLinkClick}
                >
                  Contacto
                </Link>
              </li>
              <li>
                <Link 
                  to="/sobre-nosotros" 
                  className={`nav-link ${isActive('/sobre-nosotros')}`}
                  onClick={handleNavLinkClick}
                >
                  Sobre Nosotros
                </Link>
              </li>
              {isAuthenticated && isAdmin && (
                <li>
                  <Link 
                    to="/admin" 
                    className={`nav-link ${isActive('/admin')}`}
                    onClick={handleNavLinkClick}
                  >
                    Administrador
                  </Link>
                </li>
              )}
            </ul>
          </nav>

          <div className="header-actions">
            <Link to="/carrito" className="btn-cart">
              <span>🛒</span>
              {cartCount > 0 && (
                <span className="cart-count">{cartCount}</span>
              )}
            </Link>
            
            {isAuthenticated && user?.activo ? (
              <>
                <span id="userNameHeader" className="user-name">
                  Bienvenido {user.nombre}
                </span>
                <button 
                  className="logout-btn"
                  onClick={() => { logout(); navigate('/'); }}
                >Cerrar sesión</button>
              </>
            ) : (
              <Link to="/login" className="btn btn-secondary" id="loginBtn">
                Iniciar Sesión
              </Link>
            )}
            
            <button 
              className="btn-menu" 
              id="menuBtn"
              onClick={toggleMenu}
            >
              {isMenuOpen ? '✕' : '☰'}
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;