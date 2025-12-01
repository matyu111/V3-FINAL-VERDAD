import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

const Footer = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubscribe = (e) => {
    e.preventDefault();
    const domain = email.split('@')[1];
    const allowedDomains = ['duoc.cl', 'gmail.com', 'profesor.duoc.cl'];
    if (allowedDomains.includes(domain)) {
      setMessage('¡Suscripción exitosa!');
      setEmail('');
    } else {
      setMessage('Email no permitido. Usa duoc.cl, gmail.com o profesor.duoc.cl');
    }
  };
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-content">
          <div className="footer-section">
            <h3>NeuroVision Networks</h3>
            <p>Líderes en tecnología de neurovisión y soluciones innovadoras. Transformamos el futuro digital, una innovación a la vez.</p>
            <div className="social-links">
              <a href="#" aria-label="Facebook">📘</a>
              <a href="https://www.instagram.com/neurovisionnetworks/" aria-label="Instagram" target="_blank">📷</a>
              <a href="#" aria-label="Twitter">🐦</a>
              <a href="#" aria-label="YouTube">📺</a>
            </div>
          </div>
          
          <div className="footer-section">
            <h4>Enlaces Rápidos</h4>
            <ul>
              <li><Link to="/">Inicio</Link></li>
              <li><Link to="/productos">Soluciones</Link></li>
              <li><Link to="/categorias">Tecnologías</Link></li>
              <li><Link to="/contacto">Contacto</Link></li>
            </ul>
          </div>
          
          <div className="footer-section">
            <h4>Tecnologías</h4>
            <ul>
              <li><Link to="/categorias#neurovision">NeuroVisión</Link></li>
              <li><Link to="/categorias#ai">Inteligencia Artificial</Link></li>
              <li><Link to="/categorias#analytics">Analytics</Link></li>
              <li><Link to="/categorias#interfaces">Interfaces Neurales</Link></li>
            </ul>
          </div>
          
          <div className="footer-section">
            <h4>Suscríbete</h4>
            <form onSubmit={handleSubscribe}>
              <input 
                type="email" 
                value={email} 
                onChange={(e) => setEmail(e.target.value)} 
                placeholder="Tu correo electrónico" 
                required 
              />
              <button type="submit">Suscribir</button>
            </form>
            {message && <p>{message}</p>}
          </div>
          
          <div className="footer-section">
            <h4>Contacto</h4>
            <div className="contact-info">
              <p>📧 info@neurovisionnetworks.com</p>
              <p>📞 +56 9 1234 5678</p>
              <p>📍 Duoc UC Maipú, Santiago</p>
              <p>🕒 Lun-Vie: 9:00-19:00</p>
            </div>
          </div>
        </div>
        
        <div className="footer-bottom">
          <p>&copy; 2025 NeuroVision Networks. Todos los derechos reservados.
            <br /> Desarrollado por Kevin y Matías.
          </p>
          <div className="footer-links">
            <Link to="/politica-privacidad">Política de Privacidad</Link>
            <Link to="/terminos-condiciones">Términos y Condiciones</Link>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;