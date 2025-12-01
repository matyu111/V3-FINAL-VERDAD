import React from 'react';
import './SobreNosotros.css'; // Ajustado el import de CSS para coincidir con la estructura
import equipoImg from '../../assets/equipo.jpg';
import especialistaImg from '../../assets/especialista.jpg';

const SobreNosotros = () => {
  return (
    <div className="about-page">
      <section className="about-hero">
        <div className="container">
          <div className="hero-content">
            <h1>Sobre NeuroVision Networks</h1>
            <p className="hero-subtitle">Líderes en tecnología de neurovisión desde 2020</p>
          </div>
        </div>
      </section>

      <section className="history-section">
        <div className="container">
          <div className="history-content">
            <div className="history-text">
              <h2>Nuestra Historia</h2>
              <p>NeuroVision Networks nació en 2020 con una visión revolucionaria: democratizar el acceso a la tecnología de neurovisión avanzada. Fundada por un equipo de científicos, ingenieros y visionarios tecnológicos, nuestra empresa surgió de la necesidad de crear soluciones innovadoras que transformen la forma en que interactuamos con la tecnología.</p>
              <p>Lo que comenzó como un proyecto de investigación en Silicon Valley, hoy se ha convertido en una de las empresas de tecnología de neurovisión más innovadoras del mundo, sirviendo a miles de organizaciones, desarrolladores y visionarios tecnológicos.</p>
              <p>Nuestro compromiso con la innovación y la excelencia técnica nos ha permitido crecer constantemente, expandiendo nuestro portafolio de soluciones y mejorando nuestras tecnologías para ofrecerte la mejor experiencia en neurovisión.</p>
            </div>
            <div className="history-image">
              <img src={equipoImg} alt="Equipo NeuroVision Networks" loading="lazy" />
            </div>
          </div>
        </div>
      </section>

      <section className="mission-section">
        <div className="container">
          <div className="mission-grid">
            <article className="mission-card">
              <div className="mission-icon">🎯</div>
              <h3>Nuestra Misión</h3>
              <p>Desarrollar y proporcionar tecnología de neurovisión de la más alta calidad, acompañada de asesoramiento experto, para ayudar a nuestros clientes a alcanzar sus objetivos tecnológicos de manera segura y efectiva.</p>
            </article>
            
            <article className="mission-card">
              <div className="mission-icon">👁️</div>
              <h3>Nuestra Visión</h3>
              <p>Ser la empresa de tecnología de neurovisión líder mundial, reconocida por nuestra excelencia en innovación, servicio al cliente y contribución al desarrollo de la tecnología del futuro.</p>
            </article>
            
            <article className="mission-card">
              <div className="mission-icon">💎</div>
              <h3>Nuestros Valores</h3>
              <ul>
                <li><strong>Innovación:</strong> Solo tecnologías de vanguardia y certificadas</li>
                <li><strong>Transparencia:</strong> Información clara y honesta sobre nuestras soluciones</li>
                <li><strong>Servicio:</strong> Soporte técnico personalizado y profesional</li>
                <li><strong>Excelencia:</strong> Siempre a la vanguardia de la tecnología</li>
              </ul>
            </article>
          </div>
        </div>
      </section>

      <section className="team-section">
        <div className="container">
          <h2>Nuestro Equipo</h2>
          <p className="team-intro">Conoce a los profesionales que hacen posible NeuroVision Networks</p>
          
          <div className="team-grid">
            <article className="team-member">
              <div className="member-photo">
                <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop&crop=face" alt="Carlos Mendoza" loading="lazy" />
              </div>
              <div className="member-info">
                <h3>Dr. Carlos Mendoza</h3>
                <p className="member-role">Fundador & CEO</p>
                <p className="member-bio">Doctor en Ciencias de la Computación con más de 15 años de experiencia en neurovisión e inteligencia artificial. Especialista en algoritmos de aprendizaje profundo y interfaces neurales.</p>
                <div className="member-credentials">
                  <span>PhD Computer Science</span>
                  <span>Stanford University</span>
                </div>
              </div>
            </article>
            
            <article className="team-member ana-lopez">  {/* Added class for specific styling */}
              <div className="member-photo">
                <img src={especialistaImg} alt="Dr. Ana López" loading="lazy" />
              </div>
              <div className="member-info">
                <h3>Dr. Ana López</h3>
                <p className="member-role">Directora de Tecnología</p>
                <p className="member-bio">Doctora en Ingeniería de Sistemas, especialista en neurovisión computacional. Responsable de la investigación y desarrollo de todas nuestras tecnologías.</p>
                <div className="member-credentials">
                  <span>PhD Systems Engineering</span>
                  <span>MIT Graduate</span>
                </div>
              </div>
            </article>
            
            <article className="team-member">
              <div className="member-photo">
                <img src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop&crop=face" alt="Miguel Rodríguez" loading="lazy" />
              </div>
              <div className="member-info">
                <h3>Miguel Rodríguez</h3>
                <p className="member-role">Director de Soluciones</p>
                <p className="member-bio">Ingeniero de Software con más de 12 años de experiencia en implementación de sistemas de neurovisión. Ayuda a nuestros clientes a elegir las soluciones más adecuadas para sus necesidades.</p>
                <div className="member-credentials">
                  <span>Software Engineer</span>
                  <span>Google Alumni</span>
                </div>
              </div>
            </article>
          </div>
        </div>
      </section>

      <section className="certifications-section">
        <div className="container">
          <h2>Certificaciones y Garantías</h2>
          <div className="certifications-grid">
            <article className="certification-card">
              <div className="cert-icon">✅</div>
              <h3>Tecnologías Auténticas</h3>
              <p>Todas nuestras tecnologías son 100% originales, desarrolladas internamente por nuestro equipo de investigación.</p>
            </article>
            
            <article className="certification-card">
              <div className="cert-icon">🔬</div>
              <h3>Control de Calidad</h3>
              <p>Rigurosos controles de calidad y pruebas exhaustivas para garantizar la precisión y efectividad de nuestras soluciones.</p>
            </article>
            
            <article className="certification-card">
              <div className="cert-icon">🏆</div>
              <h3>Certificaciones</h3>
              <p>Tecnologías certificadas por organismos internacionales como IEEE, ISO y organismos de seguridad cibernética.</p>
            </article>
            
            <article className="certification-card">
              <div className="cert-icon">🛡️</div>
              <h3>Garantía de Satisfacción</h3>
              <p>90 días de garantía de satisfacción si no estás completamente satisfecho con nuestras soluciones.</p>
            </article>
          </div>
        </div>
      </section>

      <section className="stats-section">
        <div className="container">
          <h2>NeuroVision Networks en Números</h2>
          <div className="stats-grid">
            <div className="stat-item">
              <div className="stat-number">5,000+</div>
              <div className="stat-label">Clientes Satisfechos</div>
            </div>
            <div className="stat-item">
              <div className="stat-number">50+</div>
              <div className="stat-label">Soluciones Disponibles</div>
            </div>
            <div className="stat-item">
              <div className="stat-number">25+</div>
              <div className="stat-label">Patentes Registradas</div>
            </div>
            <div className="stat-item">
              <div className="stat-number">4</div>
              <div className="stat-label">Años de Experiencia</div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default SobreNosotros;