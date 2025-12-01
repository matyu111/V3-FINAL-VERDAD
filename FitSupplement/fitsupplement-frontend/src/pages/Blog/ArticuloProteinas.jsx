import React from 'react';
import './Blog.css'; // Reutilizamos estilos

const ArticuloProteinas = () => {
  return (
    <main className="main">
      <article className="blog-article">
        <div className="container">
          <h1>Proteínas: ¿Cuánta Necesitas Realmente?</h1>
          <div className="article-meta">
            <span>Nutrición</span>
            <span>18 de Enero, 2025</span>
            <span>5 min lectura</span>
          </div>
          <img src="https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=800" alt="Proteínas" loading="lazy" />
          <p>Las proteínas son macronutrientes esenciales compuestos por aminoácidos que juegan un papel crucial en la construcción y reparación de tejidos, incluyendo los músculos. Son fundamentales para cualquier persona activa en el fitness.</p>
          <h2>¿Cuánta proteína necesitas?</h2>
          <p>La cantidad recomendada varía según el peso corporal, nivel de actividad y objetivos. Para atletas, se sugiere 1.6-2.2 gramos por kg de peso corporal al día.</p>
          <h2>Fuentes de proteína</h2>
          <ul>
            <li>Animales: Carne, pollo, pescado, huevos, lácteos</li>
            <li>Vegetales: Legumbres, nueces, semillas, quinoa</li>
            <li>Suplementos: Whey protein, caseína, proteínas vegetales</li>
          </ul>
          <h2>Beneficios de un alto consumo de proteínas</h2>
          <p>Ayuda en la recuperación muscular, mantiene la saciedad, apoya la pérdida de grasa y preserva la masa muscular durante déficits calóricos.</p>
          <a href="/blog" className="btn btn-secondary">Volver al Blog</a>
        </div>
      </article>
    </main>
  );
};

export default ArticuloProteinas;