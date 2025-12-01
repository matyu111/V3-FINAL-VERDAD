import React from 'react';
import { Link } from 'react-router-dom';
import './Hero.css';

const Hero = () => {
  return (
    <section className="hero">
      <div className="container">
        <div className="hero-content">
          <div className="hero-text">
            <h2>Revoluciona el futuro con tecnología de neurovisión</h2>
            <p>Descubre nuestras soluciones innovadoras en neurovisión y tecnología avanzada, diseñadas para transformar la forma en que interactuamos con el mundo digital.</p>
            <div className="hero-buttons">
              <Link to="/productos" className="btn btn-primary">Ver Soluciones</Link>
              <Link to="#video-section" className="btn btn-secondary">Ver Demo</Link>
            </div>
          </div>
          <div className="hero-image">
            <img 
              src="https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=600&amp;h=400&amp;fit=crop" 
              alt="Suplementos para fitness" 
              loading="lazy" 
            />
          </div>
        </div>
      </div>
    </section>
  );
};

export default Hero;