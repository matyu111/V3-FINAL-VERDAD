import React from 'react';
import { Link } from 'react-router-dom';
import './GymSection.css';

const GymSection = () => {
  return (
    <section className="gym-section">
      <div className="container">
        <div className="gym-content">
          <div className="gym-text">
            <h2>Tu Espacio Fitness</h2>
            <p>Un espacio dedicado a maximizar tu potencial con nuestros suplementos premium. Aquí te ayudamos a alcanzar tus metas de fitness con productos innovadores y efectivos.</p>
            <div className="gym-features">
              <div className="gym-feature">
                <span className="feature-icon">🏋️‍♂️</span>
                <span>Entrenamiento Optimizado</span>
              </div>
              <div className="gym-feature">
                <span className="feature-icon">🔬</span>
                <span>Suplementos Científicos</span>
              </div>
              <div className="gym-feature">
                <span className="feature-icon">⚡</span>
                <span>Energía Constante</span>
              </div>
            </div>
            <Link to="/contacto" className="btn btn-primary">Explorar Suplementos</Link>
          </div>
          <div className="gym-image">
            <img 
              src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=600&amp;h=400&amp;fit=crop" 
              alt="Espacio de neurovisión con tecnología NeuroVision Networks" 
              loading="lazy" 
            />
          </div>
        </div>
      </div>
    </section>
  );
};

export default GymSection;