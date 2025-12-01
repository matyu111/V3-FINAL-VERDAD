import React, { useState } from 'react';
import './Blog.css'; // Asumiendo que migraremos los estilos

const Blog = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubscribe = (e) => {
    e.preventDefault();
    const domain = email.split('@')[1];
    if (domain === 'duoc.cl' || domain === 'gmail.com' || domain === 'profesor.duoc.cl') {
      setMessage('¡Suscripción exitosa!');
    } else {
      setMessage('Por favor, usa un email válido (duoc.cl, gmail.com o profesor.duoc.cl)');}
  };
  return (
    <main className="main">
      {/* Hero del blog */}
      <section className="blog-hero">
        <div className="container">
          <div className="hero-content">
            <h1>Blog FitSupplement</h1>
            <p className="hero-subtitle">Consejos, guías y las últimas tendencias en fitness y suplementación</p>

          </div>
        </div>
      </section>

      {/* Filtros del blog */}
      

      {/* Contenido del blog */}
      <section className="blog-content">
        <div className="container">
          <div className="blog-layout">
            {/* Artículos principales */}
            <div className="blog-main">
              {/* Artículo destacado */}
              <article className="featured-article">
                <div className="article-image">
                  <img src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&h=400&fit=crop" alt="Artículo destacado" loading="lazy" />
                  <div className="article-badge">Destacado</div>
                </div>
                <div className="article-content">
                  <div className="article-meta">
                    <span className="article-category">Suplementos</span>
                    <span className="article-date">20 de Enero, 2025</span>
                    <span className="article-read-time">5 min lectura</span>
                  </div>
                  <h2>Guía Completa de Creatina: Todo lo que Necesitas Saber</h2>
                  <p>La creatina es uno de los suplementos más estudiados científicamente. Descubre cómo funciona, cuándo tomarla y qué beneficios reales puede aportar a tu rendimiento deportivo.</p>
                  <div className="article-footer">
                    <div className="article-author">
                      <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=40&h=40&fit=crop&crop=face" alt="Autor" loading="lazy" />
                      <span>Dr. Carlos Mendoza</span>
                    </div>
                    <a href="/blog/creatina" className="btn btn-primary">Leer Más</a>
                  </div>
                </div>
              </article>

              {/* Grid de artículos */}
              <div className="articles-grid">
                <article className="article-card" data-category="nutricion">
                  <div className="article-image">
                    <img src="https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=400&h=250&fit=crop" alt="Proteínas" loading="lazy" />
                    <div className="article-badge">Popular</div>
                  </div>
                  <div className="article-content">
                    <div className="article-meta">
                      <span className="article-category">Nutrición</span>
                      <span className="article-date">18 de Enero, 2025</span>
                    </div>
                    <h3>Proteínas: ¿Cuánta Necesitas Realmente?</h3>
                    <p>Descubre la cantidad óptima de proteínas según tu objetivo y tipo de entrenamiento.</p>
                    <div className="article-footer">
                      <div className="article-stats">
                        <span>👁️ 2.5k</span>
                        <span>❤️ 180</span>
                      </div>
                      <a href="/blog/proteinas" className="btn btn-primary">Leer Más</a>
                    </div>
                  </div>
                </article>

                {/* Agrega los otros artículos de manera similar */}
                {/* ... (para no extender demasiado, asume que se agregan los demás) */}
              </div>

              {/* Paginación */}
              <div className="pagination">
                <button className="pagination-btn disabled">← Anterior</button>
                <button className="pagination-btn active">1</button>
                <button className="pagination-btn disabled">2</button>
                <button className="pagination-btn disabled">3</button>
                <button className="pagination-btn disabled">Siguiente →</button>
              </div>
            </div>

            {/* Sidebar */}
            <div className="blog-sidebar">
              

              {/* Newsletter */}
              <div className="sidebar-widget newsletter-widget">
                <h3>Newsletter</h3>
                <p>Regístrate para recibir directamente en tu email consejos y guías sobre fitness y suplementación</p>
                <form onSubmit={handleSubscribe}>
                  <input 
                    type="email" 
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                    placeholder="tucorreo@dominio.com" 
                    required 
                  />
                  <button type="submit" className="btn btn-primary">Suscribirse</button>
                </form>
                {message && <p>{message}</p>}
              </div>


            </div>
          </div>
        </div>
      </section>
    </main>
  );
};

export default Blog;