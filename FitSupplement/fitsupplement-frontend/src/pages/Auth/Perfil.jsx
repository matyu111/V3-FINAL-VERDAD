import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../../styles/Perfil.css'; // Asumiremos que crearemos este archivo basado en styles.css

const Perfil = () => {
  const [userData, setUserData] = useState(null);
  const [activeSection, setActiveSection] = useState('personal-info');
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    // Simular carga de datos del usuario (integrar con backend después)
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (!currentUser) {
      window.location.href = '/login';
      return;
    }
    
    // Cargar datos completos
    const users = JSON.parse(localStorage.getItem('users')) || [];
    const user = users.find(u => u.email === currentUser.email) || {
      nombre: 'Carlos',
      apellido: 'Mendoza',
      email: currentUser.email,
      telefono: '',
      fechaNacimiento: '',
      objetivos: ''
    };
    setUserData(user);
  }, []);

  const handleSectionChange = (section) => {
    setActiveSection(section);
  };

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleCancel = () => {
    setIsEditing(false);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const updatedData = {
      ...userData,
      nombre: formData.get('firstName'),
      apellido: formData.get('lastName'),
      telefono: formData.get('phone'),
      fechaNacimiento: formData.get('birthDate'),
      objetivos: formData.get('fitnessGoals')
    };
    
    // Actualizar localStorage (simulado)
    const users = JSON.parse(localStorage.getItem('users')) || [];
    const index = users.findIndex(u => u.email === userData.email);
    if (index !== -1) {
      users[index] = updatedData;
    } else {
      users.push(updatedData);
    }
    localStorage.setItem('users', JSON.stringify(users));
    
    setUserData(updatedData);
    setIsEditing(false);
    // Mostrar notificación de éxito
    alert('Información actualizada correctamente');
  };

  if (!userData) return <div>Cargando...</div>;

  return (
    <div className="profile-section">
      <div className="container">
        <div className="profile-container">
          {/* Sidebar */}
          <div className="profile-sidebar">
            <div className="profile-header">
              <div className="profile-avatar">
                <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop&crop=face" alt="Avatar" />
                <button className="avatar-edit">📷</button>
              </div>
              <h2>{`${userData.nombre} ${userData.apellido}`}</h2>
              <p className="user-email">{userData.email}</p>
              <div className="user-stats">
                <div className="stat"><span className="stat-number">12</span><span className="stat-label">Pedidos</span></div>
                <div className="stat"><span className="stat-number">1,250</span><span className="stat-label">Puntos</span></div>
                <div className="stat"><span className="stat-number">5</span><span className="stat-label">Favoritos</span></div>
              </div>
            </div>
            <nav className="profile-nav">
              <ul>
                <li><a onClick={() => handleSectionChange('personal-info')} className={activeSection === 'personal-info' ? 'active' : ''}>Información Personal</a></li>
                <li><a onClick={() => handleSectionChange('orders')} className={activeSection === 'orders' ? 'active' : ''}>Mis Pedidos</a></li>
                <li><a onClick={() => handleSectionChange('favorites')} className={activeSection === 'favorites' ? 'active' : ''}>Favoritos</a></li>
                <li><a onClick={() => handleSectionChange('addresses')} className={activeSection === 'addresses' ? 'active' : ''}>Direcciones</a></li>
                <li><a onClick={() => handleSectionChange('points')} className={activeSection === 'points' ? 'active' : ''}>Puntos y Recompensas</a></li>
                <li><a onClick={() => handleSectionChange('notifications')} className={activeSection === 'notifications' ? 'active' : ''}>Notificaciones</a></li>
                <li><a onClick={() => handleSectionChange('security')} className={activeSection === 'security' ? 'active' : ''}>Seguridad</a></li>
              </ul>
            </nav>
          </div>

          {/* Contenido */}
          <div className="profile-content">
            {activeSection === 'personal-info' && (
              <div className="profile-section-content active">
                <div className="section-header">
                  <h2>Información Personal</h2>
                  {!isEditing && <button className="btn btn-secondary" onClick={handleEdit}>Editar</button>}
                </div>
                <form className="profile-form" onSubmit={handleSubmit}>
                  <div className="form-group">
                    <label>Nombre</label>
                    <input name="firstName" defaultValue={userData.nombre} disabled={!isEditing} required />
                  </div>
                  <div className="form-group">
                    <label>Apellido</label>
                    <input name="lastName" defaultValue={userData.apellido} disabled={!isEditing} required />
                  </div>
                  <div className="form-group">
                    <label>Email</label>
                    <input type="email" defaultValue={userData.email} disabled />
                  </div>
                  <div className="form-group">
                    <label>Teléfono</label>
                    <input name="phone" defaultValue={userData.telefono} disabled={!isEditing} />
                  </div>
                  <div className="form-group">
                    <label>Fecha de Nacimiento</label>
                    <input type="date" name="birthDate" defaultValue={userData.fechaNacimiento} disabled={!isEditing} />
                  </div>
                  <div className="form-group">
                    <label>Objetivos Fitness</label>
                    <select name="fitnessGoals" defaultValue={userData.objetivos} disabled={!isEditing}>
                      <option value="">Selecciona</option>
                      <option value="perder-peso">Perder peso</option>
                      <option value="ganar-musculo">Ganar músculo</option>
                      <option value="mantener">Mantener</option>
                    </select>
                  </div>
                  {isEditing && (
                    <div className="form-actions">
                      <button type="submit" className="btn btn-primary">Guardar</button>
                      <button type="button" className="btn btn-secondary" onClick={handleCancel}>Cancelar</button>
                    </div>
                  )}
                </form>
              </div>
            )}

            {/* Otras secciones similares, implementadas de manera básica por ahora */}
            {activeSection === 'orders' && <div>Mis Pedidos (Implementar lista de pedidos)</div>}
            {activeSection === 'favorites' && <div>Favoritos (Implementar grid de favoritos)</div>}
            {activeSection === 'addresses' && <div>Direcciones (Implementar gestión de direcciones)</div>}
            {activeSection === 'points' && <div>Puntos y Recompensas (Implementar sistema de puntos)</div>}
            {activeSection === 'notifications' && <div>Notificaciones (Implementar configuración)</div>}
            {activeSection === 'security' && <div>Seguridad (Implementar cambio de contraseña y 2FA)</div>}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Perfil;