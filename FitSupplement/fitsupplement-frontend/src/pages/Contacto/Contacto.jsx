// Remove Header and Footer
import React, { useState } from 'react';
import FormGroup from '../../components/FormGroup';
import './Contacto.css';

const Contacto = () => {
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    telefono: '',
    asunto: '',
    mensaje: '',
    acepto: false,
    privacidad: false
  });

  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.nombre) newErrors.nombre = 'El nombre es requerido';
    else if (formData.nombre.length < 10) newErrors.nombre = 'El nombre debe tener al menos 10 caracteres';
    if (!formData.email) newErrors.email = 'El email es requerido';
    else {
      const domain = formData.email.split('@')[1];
      const validDomains = ['duoc.cl', 'gmail.com', 'profesor.duoc.cl'];
      if (!validDomains.includes(domain)) newErrors.email = 'Por favor, usa un email válido (@duoc.cl, @gmail.com o @profesor.duoc.cl)';
      else if (!/\S+@\S+\.\S+/.test(formData.email)) newErrors.email = 'Email inválido';
    }
    if (formData.telefono && !/^\+?\d{9,12}$/.test(formData.telefono)) newErrors.telefono = 'Teléfono inválido (9-12 dígitos)';
    if (!formData.asunto) newErrors.asunto = 'El asunto es requerido';
    if (!formData.mensaje) newErrors.mensaje = 'El mensaje es requerido';
    else if (formData.mensaje.length < 40) newErrors.mensaje = 'El mensaje debe tener al menos 40 caracteres';
    else if (formData.mensaje.length > 500) newErrors.mensaje = 'El mensaje no debe exceder 500 caracteres';
    if (!formData.privacidad) newErrors.privacidad = 'Debes aceptar la política de privacidad';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      // Aquí iría la lógica de envío, por ahora simulamos éxito
      setSuccess(true);
      setFormData({
        nombre: '',
        email: '',
        telefono: '',
        asunto: '',
        mensaje: '',
        acepto: false,
        privacidad: false
      });
    }
  };

  return (
    <main className="main">
      <section className="contact-section">
        <div className="container">
          <h1>Contáctanos</h1>
          <p className="contact-intro">¿Tienes preguntas sobre nuestros productos? ¿Necesitas asesoramiento personalizado? Estamos aquí para ayudarte.</p>
          
          <div className="contact-content">
            <div className="contact-info">
              <h2>Información de Contacto</h2>
              <div className="contact-details">
                <div className="contact-item">
                  <div className="contact-icon">📧</div>
                  <div>
                    <h3>Email</h3>
                    <p>info@neurovisionnetworks.com</p>
                    <p>ventas@neurovisionnetworks.com</p>
                  </div>
                </div>
                
                <div className="contact-item">
                  <div className="contact-icon">📞</div>
                  <div>
                    <h3>Teléfono</h3>
                    <p>+56 9 1234 5678</p>
                    <p>+56 2 2345 6789</p>
                  </div>
                </div>
                
                <div className="contact-item">
                  <div className="contact-icon">📍</div>
                  <div>
                    <h3>Dirección</h3>
                    <p>Av. Providencia 1234</p>
                    <p>Santiago, Chile</p>
                  </div>
                </div>
                
                <div className="contact-item">
                  <div className="contact-icon">🕒</div>
                  <div>
                    <h3>Horarios</h3>
                    <p>Lunes - Viernes: 9:00 - 19:00</p>
                    <p>Sábados: 10:00 - 16:00</p>
                  </div>
                </div>
              </div>
            </div>

            <div className="contact-form-container">
              <h2>Envíanos un Mensaje</h2>
              <form className="contact-form" onSubmit={handleSubmit} noValidate>
                <div className="form-group">
                  <label htmlFor="nombre">Nombre completo *</label>
                  <input
                    type="text"
                    id="nombre"
                    name="nombre"
                    value={formData.nombre}
                    onChange={handleChange}
                    required
                    placeholder="Ingresa tu nombre completo"
                    className={errors.nombre ? 'error' : ''}
                  />
                  {errors.nombre && <span className="error-message show">{errors.nombre}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="email">Correo electrónico *</label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                    placeholder="tu@email.com"
                    className={errors.email ? 'error' : ''}
                  />
                  {errors.email && <span className="error-message show">{errors.email}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="telefono">Teléfono</label>
                  <input
                    type="tel"
                    id="telefono"
                    name="telefono"
                    value={formData.telefono}
                    onChange={handleChange}
                    placeholder="+56 9 1234 5678"
                    className={errors.telefono ? 'error' : ''}
                  />
                  {errors.telefono && <span className="error-message show">{errors.telefono}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="asunto">Asunto *</label>
                  <select 
                    id="asunto" 
                    name="asunto" 
                    value={formData.asunto}
                    onChange={handleChange}
                    required
                    className={errors.asunto ? 'error' : ''}
                  >
                    <option value="">Selecciona un asunto</option>
                    <option value="consulta-producto">Consulta sobre producto</option>
                    <option value="asesoramiento">Asesoramiento personalizado</option>
                    <option value="problema-pedido">Problema con pedido</option>
                    <option value="devolucion">Devolución o cambio</option>
                    <option value="sugerencia">Sugerencia</option>
                    <option value="otro">Otro</option>
                  </select>
                  {errors.asunto && <span className="error-message show">{errors.asunto}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="mensaje">Mensaje *</label>
                  <textarea 
                    id="mensaje" 
                    name="mensaje"
                    value={formData.mensaje}
                    onChange={handleChange}
                    required
                    rows="5"
                    placeholder="Escribe tu mensaje aquí..."
                    className={errors.mensaje ? 'error' : ''}
                  ></textarea>
                  {errors.mensaje && <span className="error-message show">{errors.mensaje}</span>}
                </div>

                <div className="form-group checkbox-group">
                  <label className="checkbox-label">
                    <input 
                      type="checkbox" 
                      id="acepto" 
                      name="acepto"
                      checked={formData.acepto}
                      onChange={handleChange}
                    />
                    <span className="checkmark"></span>
                    Acepto recibir información sobre ofertas y nuevos productos
                  </label>
                  {errors.acepto && <span className="error-message show">{errors.acepto}</span>}
                </div>

                <div className="form-group checkbox-group">
                  <label className="checkbox-label">
                    <input 
                      type="checkbox" 
                      id="privacidad" 
                      name="privacidad"
                      checked={formData.privacidad}
                      onChange={handleChange}
                      required
                    />
                    <span className="checkmark"></span>
                    He leído y acepto la <a href="politica-privacidad.html" target="_blank">Política de Privacidad</a> *
                  </label>
                  {errors.privacidad && <span className="error-message show">{errors.privacidad}</span>}
                </div>

                <button type="submit" className="btn btn-primary btn-submit">
                  <span className="btn-text">Enviar Mensaje</span>
                </button>
              </form>

              {success && (
                <div className="modal-overlay">
                  <div className="modal-content">
                    <div className="success-icon">✅</div>
                    <h3>¡Mensaje enviado con éxito!</h3>
                    <p>Nos pondremos en contacto pronto. ¡Gracias!</p>
                    <button onClick={() => setSuccess(false)} className="btn btn-primary">Cerrar</button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </section>

        <section className="faq-section">
          <div className="container">
            <h2>Preguntas Frecuentes</h2>
            <div className="faq-grid">
              <article className="faq-item">
                <h3>¿Cuánto tiempo tarda el envío?</h3>
                <p>Los envíos dentro de Santiago se realizan en 24-48 horas. Para regiones, el tiempo de entrega es de 3-5 días hábiles.</p>
              </article>
              
              <article className="faq-item">
                <h3>¿Ofrecen asesoramiento personalizado?</h3>
                <p>Sí, nuestro equipo de expertos en nutrición deportiva está disponible para ayudarte a elegir los suplementos más adecuados para tus objetivos.</p>
              </article>
              
              <article className="faq-item">
                <h3>¿Los productos son originales?</h3>
                <p>Todos nuestros productos son 100% originales y cuentan con certificaciones de calidad. Trabajamos directamente con los distribuidores oficiales.</p>
              </article>
              
              <article className="faq-item">
                <h3>¿Tienen garantía de devolución?</h3>
                <p>Sí, ofrecemos 30 días de garantía de devolución si no estás satisfecho con tu compra, siempre que el producto esté en su empaque original.</p>
              </article>
            </div>
          </div>
        </section>
      </main>
    );
};

export default Contacto;