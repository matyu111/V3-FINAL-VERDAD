import React from 'react';
import { Link } from 'react-router-dom';
import './Categorias.css';

const categoriesData = [
  {
    id: 'proteinas',
    title: 'Proteínas',
    description: 'Suplementos proteicos de alta calidad para maximizar la recuperación y el crecimiento muscular. Incluye whey, caseína y proteínas vegetales.',
    image: 'https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400&h=300&fit=crop',
    benefits: [
      'Recuperación muscular acelerada',
      'Crecimiento y mantenimiento muscular',
      'Control del apetito',
      'Mejora de la composición corporal'
    ],
    stats: { products: '15+', priceFrom: '$19.990' },
    link: '/productos?categoria=proteinas'
  },
  {
    id: 'creatina',
    title: 'Creatina',
    description: 'El suplemento más estudiado científicamente para aumentar la fuerza, potencia y masa muscular. Disponible en diferentes formas.',
    image: 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&h=300&fit=crop',
    benefits: [
      'Aumento de fuerza y potencia',
      'Mejora del rendimiento en ejercicios de alta intensidad',
      'Incremento de la masa muscular',
      'Recuperación más rápida entre series'
    ],
    stats: { products: '8+', priceFrom: '$15.990' },
    link: '/productos?categoria=creatina'
  },
  {
    id: 'pre-entreno',
    title: 'Pre-entreno',
    description: 'Suplementos diseñados para maximizar tu energía, enfoque y rendimiento durante los entrenamientos más intensos.',
    image: 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop',
    benefits: [
      'Energía sostenida durante el entrenamiento',
      'Mayor enfoque y concentración',
      'Mejora de la resistencia',
      'Reducción de la fatiga'
    ],
    stats: { products: '12+', priceFrom: '$18.990' },
    link: '/productos?categoria=pre-entreno'
  },
  {
    id: 'vitaminas',
    title: 'Vitaminas y Minerales',
    description: 'Suplementos vitamínicos y minerales esenciales para mantener una salud óptima y apoyar el rendimiento deportivo.',
    image: 'https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400&h=300&fit=crop',
    benefits: [
      'Apoyo al sistema inmunológico',
      'Mejora de la energía y vitalidad',
      'Salud ósea y articular',
      'Recuperación y bienestar general'
    ],
    stats: { products: '20+', priceFrom: '$8.990' },
    link: '/productos?categoria=vitaminas'
  },
  {
    id: 'aminoacidos',
    title: 'Aminoácidos',
    description: 'Bloques de construcción de las proteínas. BCAA, EAA y aminoácidos individuales para optimizar la síntesis proteica.',
    image: 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&h=300&fit=crop',
    benefits: [
      'Prevención del catabolismo muscular',
      'Mejora de la síntesis proteica',
      'Reducción del dolor muscular',
      'Mejora de la resistencia'
    ],
    stats: { products: '10+', priceFrom: '$12.990' },
    link: '/productos?categoria=aminoacidos'
  },
  {
    id: 'quemadores',
    title: 'Quemadores de Grasa',
    description: 'Suplementos termogénicos y lipotrópicos para acelerar el metabolismo y apoyar la pérdida de grasa corporal.',
    image: 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop',
    benefits: [
      'Aceleración del metabolismo',
      'Mayor quema de grasa',
      'Supresión del apetito',
      'Energía para entrenamientos'
    ],
    stats: { products: '6+', priceFrom: '$22.990' },
    link: '/productos?categoria=quemadores'
  }
];

const guideData = [
  {
    icon: '🎯',
    title: 'Define tus Objetivos',
    description: 'Antes de elegir un suplemento, identifica claramente qué quieres lograr: ganar masa muscular, perder grasa, mejorar el rendimiento, etc.'
  },
  {
    icon: '📊',
    title: 'Evalúa tu Rutina',
    description: 'Considera tu nivel de entrenamiento, frecuencia de ejercicio y tipo de actividades que realizas para elegir los suplementos más adecuados.'
  },
  {
    icon: '🍽️',
    title: 'Complementa tu Dieta',
    description: 'Los suplementos deben complementar, no reemplazar, una alimentación balanceada. Prioriza siempre la comida real.'
  },
  {
    icon: '👨‍⚕️',
    title: 'Consulta a un Experto',
    description: 'Si tienes dudas, consulta con un nutricionista deportivo o nuestro equipo de asesores para una recomendación personalizada.'
  }
];

const Categorias = () => {
  return (
    <main className="main">
      <section className="categories-section">
        <div className="container">
          <h1>Nuestras Categorías</h1>
          <p className="categories-intro">Descubre nuestra amplia gama de suplementos organizados por categorías para facilitar tu búsqueda.</p>
          <div className="categories-grid">
            {categoriesData.map((category) => (
              <article key={category.id} className="category-card" id={category.id}>
                <div className="category-image">
                  <img src={category.image} alt={category.title} loading="lazy" />
                </div>
                <div className="category-content">
                  <h2>{category.title}</h2>
                  <p>{category.description}</p>
                  <ul className="category-benefits">
                    {category.benefits.map((benefit, index) => (
                      <li key={index}>{benefit}</li>
                    ))}
                  </ul>
                  <div className="category-stats">
                    <span>{category.stats.products} productos</span>
                    <span>Desde {category.stats.priceFrom}</span>
                  </div>
                  <Link to={category.link} className="btn btn-primary">Ver {category.title}</Link>
                </div>
              </article>
            ))}
          </div>
        </div>
        </section>

        <section className="guide-section">
          <div className="container">
            <h2>Guía de Compra</h2>
            <div className="guide-grid">
              {guideData.map((guide, index) => (
                <article key={index} className="guide-card">
                  <div className="guide-icon">{guide.icon}</div>
                  <h3>{guide.title}</h3>
                  <p>{guide.description}</p>
                </article>
              ))}
            </div>
          </div>
        </section>
      </main>
  );
};

export default Categorias;