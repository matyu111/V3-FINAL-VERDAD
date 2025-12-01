import React from 'react';
import Header from './Header';
import Footer from './Footer';

import '../App.css'; // Asegúrate de importar estilos globales si es necesario

const Layout = ({ children }) => {
  return (
    <>
      <Header />
      {children}
      <Footer />
    </>
  );
};

export default Layout;