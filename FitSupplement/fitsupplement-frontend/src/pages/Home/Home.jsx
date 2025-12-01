import React from 'react';
import Hero from '../../components/Hero/Hero';
import Features from '../../components/Features/Features';
import SolucionesDestacadas from '../../components/Products/SolucionesDestacadas';
import Testimonials from '../../components/Testimonials/Testimonials';
import Gimnasio from '../../components/Gimnasio/Gimnasio';
import './Home.css';

const Home = () => {
  return (
    <main className="main">
      <Hero />
      <Features />
      <Gimnasio />
      <SolucionesDestacadas />
      <Testimonials />
    </main>
  );
};

export default Home;