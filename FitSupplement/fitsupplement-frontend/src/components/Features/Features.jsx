import React from 'react';
import './Features.css';

const Features = () => {
  const featuresData = [
    {
      icon: '🧠',
      title: 'Tecnología Avanzada',
      description: 'Soluciones de neurovisión de última generación con algoritmos de inteligencia artificial.'
    },
    {
      icon: '⚡',
      title: 'Implementación Rápida',
      description: 'Despliegue rápido y eficiente de nuestras soluciones tecnológicas en tu organización.'
    },
    {
      icon: '🎯',
      title: 'Asesoramiento Experto',
      description: 'Nuestros especialistas te ayudan a elegir la solución perfecta para tus necesidades.'
    },
    {
      icon: '🔮',
      title: 'Innovación Constante',
      description: 'Estamos a la vanguardia de la tecnología, desarrollando el futuro de la neurovisión.'
    }
  ];

  return (
    <section className="features">
      <div className="container">
        <h2>¿Por qué elegir NeuroVision Networks?</h2>
        <div className="features-grid">
          {featuresData.map((feature, index) => (
            <article key={index} className="feature-card">
              <div className="feature-icon">{feature.icon}</div>
              <h3>{feature.title}</h3>
              <p>{feature.description}</p>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Features;