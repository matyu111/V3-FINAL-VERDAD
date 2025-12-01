import React from 'react';
import './Gimnasio.css'; // Asegúrate de crear este archivo CSS si es necesario
import gymm from '../../assets/gymm.jpg';

const Gimnasio = () => {
  return (
    <section className="gimnasio-section">
      <img src={gymm} alt="Máquina de gimnasio con barra en interior oscuro" className="gimnasio-image" />
      <h2>Gimnasio</h2>
      <p>Un espacio dedicado al desarrollo y prueba de nuestras tecnologías de neurovisión más avanzadas. Aquí experimentamos, innovamos y creamos el futuro de la tecnología.</p>
      <ul>
        <li>🏋️‍♂️ Entrenamiento Tecnológico</li>
        <li>🔬 Laboratorio de Pruebas</li>
        <li>⚡ Innovación Constante</li>
      </ul>
      <button>Visitar Gimnasio</button>
    </section>
  );
};

export default Gimnasio;