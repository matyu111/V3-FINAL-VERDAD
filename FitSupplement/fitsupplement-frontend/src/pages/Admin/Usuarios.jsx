import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllUsers, toggleUser, createUser, updateUser, deleteUser } from '../../services/adminService';
import '../../styles/Usuarios.css';

const Usuarios = () => {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  // Oculta el navbar mientras el modal de edición/creación está abierto
  useEffect(() => {
    if (showModal) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'auto';
    }
    return () => {
      document.body.style.overflow = 'auto';
    };
  }, [showModal]);
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    role: 'USER',
    telefono: '',
    fechaNacimiento: '',
    direccion: '',
    objetivos: '',
    aceptoMarketing: false
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await getAllUsers();
      setUsers(response.data);
      setLoading(false);
    } catch {
      setError('Error al cargar usuarios');
      setLoading(false);
    }
  };

  const handleToggle = async (id, activo) => {
    try {
      await toggleUser(id);
      setUsers(users.map(u => u.id === id ? { ...u, activo: !activo } : u));
    } catch {
      alert('Error al actualizar usuario');
    }
  };

  // Ocultar usuario placeholder del sistema
  const visibleUsers = users.filter(u => (u.email || '').toLowerCase() !== 'deleted@system.local');
  const filteredUsers = visibleUsers.filter(user =>
    `${user.nombre} ${user.apellido}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const openModal = (user = null) => {
    setEditingUser(user);
    setFormData(user ? {
      nombre: user.nombre,
      apellido: user.apellido,
      email: user.email,
      password: '',
      role: user.role,
      telefono: user.telefono || '',
      fechaNacimiento: user.fechaNacimiento || '',
      direccion: user.direccion || '',
      objetivos: user.objetivos || '',
      aceptoMarketing: !!user.aceptoMarketing
    } : {
      nombre: '',
      apellido: '',
      email: '',
      password: '',
      role: 'USER',
      telefono: '',
      fechaNacimiento: '',
      direccion: '',
      objetivos: '',
      aceptoMarketing: false
    });
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditingUser(null);
  };

  const handleInputChange = (e) => {
    const { name, type, value, checked } = e.target;
    setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingUser) {
        const updated = await updateUser(editingUser.id, formData);
        setUsers(users.map(u => u.id === editingUser.id ? updated.data : u));
      } else {
        const created = await createUser(formData);
        setUsers([...users, created.data]);
      }
      closeModal();
    } catch (err) {
      alert('Error al guardar usuario: ' + (err?.response?.data?.message || err?.message || 'Desconocido'));
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Seguro que quieres eliminar este usuario?')) {
      try {
        await deleteUser(id);
        setUsers(users.filter(u => u.id !== id));
      } catch (err) {
        const status = err?.response?.status;
        const msg = err?.response?.data?.error;
        if (status === 409) {
          alert(msg || 'No se puede eliminar el usuario: existen compras asociadas.');
        } else if (status === 403) {
          alert('Acceso prohibido: permisos insuficientes para eliminar usuarios.');
        } else {
          alert('Error al eliminar usuario');
        }
      }
    }
  };

  if (loading) return <div>Cargando usuarios...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="admin-users">
      <h1>Gestión de Usuarios</h1>
      <div className="controls">
        <button onClick={() => navigate('/admin')} className="add-btn">Volver</button>
        <input
          type="text"
          placeholder="Buscar por nombre o email..."
          value={searchTerm}
          onChange={handleSearch}
          className="search-input"
        />
        <button onClick={() => openModal()} className="add-btn">Agregar Usuario</button>
      </div>
      <div className="table-responsive">
      <table className="admin-table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Email</th>
            <th>Rol</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {filteredUsers.map(user => (
            <tr key={user.id}>
              <td>{`${user.nombre} ${user.apellido}`}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>{user.activo ? 'Activo' : 'Inactivo'}</td>
              <td>
                <button className={`btn-toggle ${user.activo ? 'btn-danger' : 'btn-success'}`} onClick={() => handleToggle(user.id, user.activo)}>
                  {user.activo ? 'Inhabilitar' : 'Habilitar'}
                </button>
                <button className="btn-edit" onClick={() => openModal(user)}>Editar</button>
                <button className="btn-delete" onClick={() => handleDelete(user.id)}>Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>

      {showModal && (
        <div 
          className="modal"
          onClick={closeModal} 
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0, 0, 0, 0.7)',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 9999
          }}
        >
          <div 
            className="modal-content" 
            onClick={(e) => e.stopPropagation()}
            style={{
              backgroundColor: 'white',
              padding: '2rem',
              borderRadius: '8px',
              width: '95%',
              maxWidth: '520px',
              maxHeight: '90vh',
              overflowY: 'auto',
              boxShadow: '0 10px 40px rgba(0, 0, 0, 0.3)'
            }}
          >
            <h2 style={{ color: '#2c3e50', marginBottom: '1.5rem', borderBottom: '2px solid #3498db', paddingBottom: '0.5rem' }}>
              {editingUser ? 'Editar Usuario' : 'Nuevo Usuario'}
            </h2>
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <input
                name="nombre"
                value={formData.nombre}
                onChange={handleInputChange}
                placeholder="Nombre"
                required
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box'
                }}
              />
              <input
                name="apellido"
                value={formData.apellido}
                onChange={handleInputChange}
                placeholder="Apellido"
                required
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box'
                }}
              />
              <input
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                placeholder="Email"
                type="email"
                required
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box'
                }}
              />
              <input
                name="telefono"
                value={formData.telefono}
                onChange={handleInputChange}
                placeholder="Teléfono"
                type="tel"
                required
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box'
                }}
              />
              <input
                name="direccion"
                value={formData.direccion}
                onChange={handleInputChange}
                placeholder="Dirección"
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box'
                }}
              />
              <input
                name="fechaNacimiento"
                value={formData.fechaNacimiento}
                onChange={handleInputChange}
                placeholder="Fecha de Nacimiento"
                type="date"
                required
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box'
                }}
              />
              <input
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                placeholder="Contraseña"
                type="password"
                required={!editingUser}
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box'
                }}
              />
              <textarea
                name="objetivos"
                value={formData.objetivos}
                onChange={handleInputChange}
                placeholder="Objetivos"
                required
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box',
                  resize: 'vertical',
                  minHeight: '80px'
                }}
              />
              <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#2c3e50', fontWeight: 500 }}>
                <input
                  type="checkbox"
                  name="aceptoMarketing"
                  checked={!!formData.aceptoMarketing}
                  onChange={handleInputChange}
                />
                Acepta marketing
              </label>
              <select 
                name="role" 
                value={formData.role} 
                onChange={handleInputChange}
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #ddd',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  fontFamily: 'inherit',
                  boxSizing: 'border-box'
                }}
              >
                <option value="USER">Usuario</option>
                <option value="ADMIN">Admin</option>
              </select>
              <button 
                type="submit" 
                style={{
                  padding: '0.75rem 1.5rem',
                  backgroundColor: '#27ae60',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  cursor: 'pointer',
                  transition: 'all 0.3s',
                  fontWeight: 500
                }}
                onMouseEnter={(e) => e.target.style.backgroundColor = '#229954'}
                onMouseLeave={(e) => e.target.style.backgroundColor = '#27ae60'}
              >
                Guardar
              </button>
              <button 
                type="button" 
                onClick={closeModal}
                style={{
                  padding: '0.75rem 1.5rem',
                  backgroundColor: '#95a5a6',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  cursor: 'pointer',
                  transition: 'all 0.3s',
                  fontWeight: 500
                }}
                onMouseEnter={(e) => e.target.style.backgroundColor = '#7f8c8d'}
                onMouseLeave={(e) => e.target.style.backgroundColor = '#95a5a6'}
              >
                Cancelar
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Usuarios;
















