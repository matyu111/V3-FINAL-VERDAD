import React from 'react';
import './FeaturedProducts.css';
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import analisi from '../../assets/analisi.jpg';
import neutra from '../../assets/neutra.jpg';
import vision from '../../assets/vision analyt.jpg';

const SolucionesDestacadas = () => {
  const products = [
    {
      id: 1,
      name: 'NeuroVision AI Platform',
      description: 'Plataforma de IA para análisis visual avanzado.',
      image: analisi,
      price: 999.99
    },
    {
      id: 2,
      name: 'Neural Interface',
      description: 'Interfaz neural para interacción hombre-máquina.',
      image: neutra,
      price: 1499.99
    },
    {
      id: 3,
      name: 'ProVision Analytics Suite',
      description: 'Suite de análisis predictivo para negocios.',
      image: vision,
      price: 1999.99
    }
  ];

  const whatsappNumber = '56932483465';
  const handleSolicitarDemo = (productName) => {
    const message = encodeURIComponent(`Hola, quiero solicitar la demo de ${productName}.`);
    const url = `https://wa.me/${whatsappNumber}?text=${message}`;
    window.open(url, '_blank');
  };

  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 3,
    slidesToScroll: 1,
    arrows: true,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 1,
        }
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1
        }
      }
    ]
  };

  return (
    <section className="featured-products">
      <div className="container">
        <h2>Soluciones Destacadas</h2>
        
        <Slider {...settings}>
          {products.map((producto) => (
            <article key={producto.id} className="product-card">
              <div className="product-image">
                <img 
                  src={producto.image} 
                  alt={producto.name} 
                  loading="lazy" 
                />
              </div>
              <div className="product-info">
                <h3>{producto.name}</h3>
                <p className="product-description">{producto.description}</p>
                <button className="btn btn-primary" onClick={() => handleSolicitarDemo(producto.name)}>
                  Solicitar Demo
                </button>
              </div>
            </article>
          ))}
        </Slider>
      </div>
    </section>
  );
};

export default SolucionesDestacadas;