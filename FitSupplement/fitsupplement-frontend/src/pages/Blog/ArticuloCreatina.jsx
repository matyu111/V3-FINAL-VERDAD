import React from 'react';
import './Blog.css'; // Reutilizamos estilos

const ArticuloCreatina = () => {
  return (
    <main className="main">
      <article className="blog-article">
        <div className="container">
          <h1>Guía Completa de Creatina: Todo lo que Necesitas Saber</h1>
          <div className="article-meta">
            <span>Suplementos</span>
            <span>20 de Enero, 2025</span>
            <span>5 min lectura</span>
          </div>
          <img src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800" alt="Creatina" loading="lazy" />
          <p>La creatina es uno de los suplementos más populares y estudiados en el mundo del fitness. Se trata de un compuesto natural que se encuentra en pequeñas cantidades en alimentos como la carne y el pescado, y que nuestro cuerpo produce en el hígado, riñones y páncreas.</p>
          <h2>¿Cómo funciona la creatina?</h2>
          <p>La creatina ayuda a regenerar el ATP (adenosín trifosfato), que es la principal fuente de energía para las contracciones musculares durante ejercicios de alta intensidad y corta duración, como el levantamiento de pesas o sprints.</p>
          <h2>Beneficios de la creatina</h2>
          <ul>
            <li>Aumento de la fuerza y potencia muscular</li>
            <li>Mejora en el rendimiento deportivo</li>
            <li>Aumento de la masa muscular</li>
            <li>Mejora en la recuperación</li>
            <li>Posibles beneficios cognitivos</li>
          </ul>
          <h2>¿Cómo tomar creatina?</h2>
          <p>La dosis recomendada es de 3-5 gramos al día. Se puede tomar con o sin fase de carga. Es importante hidratarse bien al consumir creatina.</p>
          <a href="/blog" className="btn btn-secondary">Volver al Blog</a>
        </div>
      </article>
    </main>
  );
};

export default ArticuloCreatina;