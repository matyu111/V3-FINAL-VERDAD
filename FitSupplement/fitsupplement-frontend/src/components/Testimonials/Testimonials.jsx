import React from 'react';
import './Testimonials.css';

const Testimonials = () => {
  const testimonialsData = [
    {
      content: 'La tecnología de NeuroVision Networks ha transformado completamente mis procesos. Increíble calidad y resultados.',
      author: 'Carlos Mendoza',
      role: 'Entrenador Personal',
      image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=60&amp;h=60&amp;fit=crop&amp;crop=face'
    },
    {
      content: 'El mejor servicio y suplementos de primera calidad. Recomendado 100%.',
      author: 'Ana López',
      role: 'Nutricionista',
      image: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=60&h=60&crop=face'
    },
    {
      content: 'Resultados rápidos y efectivos. Mi rendimiento ha mejorado exponencialmente.',
      author: 'Miguel Rodríguez',
      role: 'Atleta Profesional',
      image: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=60&amp;h=60&amp;fit=crop&amp;crop=face'
    }
  ];

  return (
    <section className="testimonials">
      <div className="container">
        <h2>Lo que dicen nuestros clientes</h2>
        <div className="testimonials-grid">
          {testimonialsData.map((testimonial, index) => (
            <article key={index} className="testimonial-card">
              <div className="testimonial-content">
                <p>"{testimonial.content}"</p>
              </div>
              <div className="testimonial-author">
                <img 
                  src={testimonial.image} 
                  alt={testimonial.author} 
                  loading="lazy" 
                />
                <div>
                  <h4>{testimonial.author}</h4>
                  <span>{testimonial.role}</span>
                </div>
              </div>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Testimonials;