import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../styles/Registro.css'; // Asumiremos que crearemos este después
import { useAuth } from '../../context/AuthContext.jsx';
import ModalConfirm from '../../components/ModalConfirm.jsx';

const Registro = () => {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    direccion: '',
    fechaNacimiento: '',
    password: '',
    confirmPassword: '',
    objetivos: '',
    aceptoTerminos: false,
    aceptoMarketing: false
  });

  const [errors, setErrors] = useState({});
  const [passwordStrength, setPasswordStrength] = useState(0);
  const [showPassword, setShowPassword] = useState(false);
  
  const [showModal, setShowModal] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));

    if (name === 'password') {
      updatePasswordStrength(value);
    }
  };

  const updatePasswordStrength = (password) => {
    let strength = 0;
    if (password.length >= 8) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^A-Za-z0-9]/.test(password)) strength++;
    setPasswordStrength(strength);
  };

  const validateForm = () => {
    let newErrors = {};
    if (!formData.nombre) newErrors.nombre = 'El nombre es requerido';
    if (!formData.apellido) newErrors.apellido = 'El apellido es requerido';
    if (!formData.email) newErrors.email = 'El email es requerido';
    else if (!/\S+@\S+\.\S+/.test(formData.email)) newErrors.email = 'Email inválido';
    else if (!formData.email.endsWith('@duoc.cl') && !formData.email.endsWith('@gmail.com') && !formData.email.endsWith('@profesor.duoc.cl')) {
      newErrors.email = 'Email debe ser de dominio @duoc.cl, @gmail.com o @profesor.duoc.cl';
    }
    if (!formData.telefono) newErrors.telefono = 'El teléfono es requerido';
    if (!formData.fechaNacimiento) newErrors.fechaNacimiento = 'La fecha de nacimiento es requerida';
    if (!formData.password) newErrors.password = 'La contraseña es requerida';
    else if (formData.password.length < 8) newErrors.password = 'La contraseña debe tener al menos 8 caracteres';
    if (formData.password !== formData.confirmPassword) newErrors.confirmPassword = 'Las contraseñas no coinciden';
    if (!formData.objetivos) newErrors.objetivos = 'Selecciona un objetivo';
    if (!formData.aceptoTerminos) newErrors.aceptoTerminos = 'Debes aceptar los términos';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      const payload = {
        nombre: formData.nombre,
        apellido: formData.apellido,
        email: formData.email,
        telefono: formData.telefono,
        direccion: formData.direccion,
        fechaNacimiento: formData.fechaNacimiento,
        password: formData.password,
        objetivos: formData.objetivos,
        aceptoMarketing: formData.aceptoMarketing
      };
      const res = await register(payload);
      if (res.ok) {
        setShowModal(true);
      } else {
        setErrors({ general: res.message });
      }
    }
  };

  return (
    <div className="register-page">
      <div className="register-container">
        <div className="register-form-container">
          <div className="register-header">
            <h1>Crea tu cuenta</h1>
            <p>Únete a nuestra comunidad fitness</p>
          </div>

          <form onSubmit={handleSubmit} className="register-form">
            <div className="form-row">
              <div className="form-group">
                <label>Nombre *</label>
                <input
                  type="text"
                  name="nombre"
                  value={formData.nombre}
                  onChange={handleChange}
                  placeholder="Ingresa tu nombre"
                />
                {errors.nombre && <span className="error">{errors.nombre}</span>}
              </div>
              <div className="form-group">
                <label>Apellido *</label>
                <input
                  type="text"
                  name="apellido"
                  value={formData.apellido}
                  onChange={handleChange}
                  placeholder="Ingresa tu apellido"
                />
                {errors.apellido && <span className="error">{errors.apellido}</span>}
              </div>
            </div>

            <div className="form-group">
              <label>Email *</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="Ingresa tu email"
              />
              {errors.email && <span className="error">{errors.email}</span>}
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Teléfono *</label>
                <input
                  type="tel"
                  name="telefono"
                  value={formData.telefono}
                  onChange={handleChange}
                  placeholder="Ingresa tu teléfono"
                />
                {errors.telefono && <span className="error">{errors.telefono}</span>}
              </div>
              <div className="form-group">
                <label>Fecha de Nacimiento *</label>
                <input
                  type="date"
                  name="fechaNacimiento"
                  value={formData.fechaNacimiento}
                  onChange={handleChange}
                />
                {errors.fechaNacimiento && <span className="error">{errors.fechaNacimiento}</span>}
              </div>
            </div>

            <div className="form-group">
              <label>Dirección</label>
              <input
                type="text"
                name="direccion"
                value={formData.direccion}
                onChange={handleChange}
                placeholder="Ingresa tu dirección"
              />
            </div>

            <div className="form-group password-input">
              <label>Contraseña *</label>
              <input
                type={showPassword ? 'text' : 'password'}
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Ingresa tu contraseña"
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? '' : ''}
              </button>
              {errors.password && <span className="error">{errors.password}</span>}
              <div className="password-strength">
                Fortaleza: 
                <div className={`strength-bar level-${passwordStrength}`}></div>
              </div>
            </div>

            <div className="form-group password-input">
              <label>Confirmar Contraseña *</label>
              <input
                type="password"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="Confirma tu contraseña"
              />
              {errors.confirmPassword && <span className="error">{errors.confirmPassword}</span>}
            </div>

            <div className="form-group">
              <label>Objetivos Fitness *</label>
              <select
                name="objetivos"
                value={formData.objetivos}
                onChange={handleChange}
              >
                <option value="">Selecciona tus objetivos</option>
                <option value="perder-peso">Perder peso</option>
                <option value="ganar-musculo">Ganar músculo</option>
                <option value="mejorar-rendimiento">Mejorar rendimiento</option>
                <option value="mantener-forma">Mantener forma</option>
              </select>
              {errors.objetivos && <span className="error">{errors.objetivos}</span>}
            </div>

            <div className="form-group checkbox-group">
              <input
                type="checkbox"
                name="aceptoTerminos"
                checked={formData.aceptoTerminos}
                onChange={handleChange}
              />
              <label>Acepto los términos y condiciones *</label>
              {errors.aceptoTerminos && <span className="error">{errors.aceptoTerminos}</span>}
            </div>

            <div className="form-group checkbox-group">
              <input
                type="checkbox"
                name="aceptoMarketing"
                checked={formData.aceptoMarketing}
                onChange={handleChange}
              />
              <label>Acepto recibir emails de marketing</label>
            </div>

          <button type="submit" className="btn-register">
            Crear Cuenta
          </button>
          {errors.general && <p className="error">{errors.general}</p>}
        </form>

          <div className="register-footer">
            <p>¿Ya tienes cuenta? <Link to="/login">Inicia sesión aquí</Link></p>
          </div>
        </div>

        <div className="register-benefits">
          <h2>Beneficios de ser miembro</h2>
          <div className="benefits-list">
            <div className="benefit-item"> Descuentos exclusivos</div>
            <div className="benefit-item"> Envío gratis</div>
            <div className="benefit-item"> Asesoramiento personalizado</div>
            <div className="benefit-item"> App móvil</div>
            <div className="benefit-item"> Programa de puntos</div>
            <div className="benefit-item"> Rutinas personalizadas</div>
          </div>
        </div>
      </div>
      {showModal && (
        <ModalConfirm
          isOpen={showModal}
          message="Registro completado correctamente"
          onConfirm={() => { setShowModal(false); navigate('/'); }}
          onClose={() => setShowModal(false)}
          showCancel={false}
        />
      )}
    </div>
  );
};

export default Registro;


