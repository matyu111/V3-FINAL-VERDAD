import React, { useState, useEffect } from 'react';

import { useNavigate } from 'react-router-dom';
import { getAllProductsAdmin, toggleProduct } from '../../services/adminService';
import { createProduct, updateProduct, deleteProduct } from '../../services/productService';
import '../../styles/ProductosAdmin.css';

const ProductosAdmin = () => {
  const CATEGORIES = [
    { value: 'proteinas', label: 'Proteínas' },
    { value: 'creatina', label: 'Creatina' },
    { value: 'pre-entreno', label: 'Pre-entreno' },
    { value: 'vitaminas', label: 'Vitaminas' },
    { value: 'aminoacidos', label: 'Aminoácidos' },
    { value: 'quemadores', label: 'Quemadores de grasa' },
  ];
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStock, setFilterStock] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [formData, setFormData] = useState({
    nombre: '',
    descripcion: '',
    precio: '',
    stock: '',
    categoria: '',
    imagen: ''
  });
  const [imageLoadOk, setImageLoadOk] = useState(true);

  const formatPriceCOP = (value) => {
    const num = Number(value ?? 0);
    return `$${num.toLocaleString('es-CO')}`;
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await getAllProductsAdmin();
      setProducts(response.data);
      setLoading(false);
      } catch {
      setError('Error al cargar productos');
      setLoading(false);
    }
  };

  const handleToggle = async (id, activo) => {
    try {
      await toggleProduct(id);
      setProducts(products.map(p => p.id === id ? { ...p, activo: !activo } : p));
      window.dispatchEvent(new Event('productsChanged'));
      } catch {
      alert('Error al actualizar producto');
    }
  };

  // Ocultar producto placeholder del sistema
  const visibleProducts = products.filter(p => {
    const isPlaceholderCategory = (p.categoria || '').toUpperCase() === 'PLACEHOLDER';
    const isPlaceholderName = (p.nombre || '').toLowerCase() === 'producto eliminado';
    return !(isPlaceholderCategory || isPlaceholderName);
  });
  const filteredProducts = visibleProducts.filter(product => {
    const matchesSearch = product.nombre.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStock = !filterStock || product.stock < 5;
    return matchesSearch && matchesStock;
  });

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const toggleStockFilter = () => {
    setFilterStock(!filterStock);
  };

  const openModal = (product = null) => {
    setEditingProduct(product);
    setFormData(product ? {
      nombre: product.nombre,
      descripcion: product.descripcion,
      precio: product.precio,
      stock: product.stock,
      categoria: product.categoria,
      imagen: product.imagen || ''
    } : {
      nombre: '',
      descripcion: '',
      precio: '',
      stock: '',
      categoria: '',
      imagen: ''
    });
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditingProduct(null);
  };

  const handleInputChange = (e) => {
    const { name, value, type } = e.target;
    let parsed = value;
    if (type === 'number') {
      parsed = name === 'stock' ? parseInt(value || '0', 10) : parseFloat(value || '0');
    }
    setFormData({ ...formData, [name]: parsed });
  };

  const handleImagenChange = (e) => {
    const val = e.target.value;
    // reset image load state — asumimos que se intentará cargar de nuevo
    setImageLoadOk(true);
    setFormData({ ...formData, imagen: val });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Bloquear envío si hay URL y la imagen no carga correctamente
    if (formData.imagen && !imageLoadOk) {
      alert('La imagen no carga correctamente. Corrige la URL antes de guardar.');
      return;
    }
    try {
      if (editingProduct) {
        const updated = await updateProduct(editingProduct.id, formData);
        setProducts(products.map(p => p.id === editingProduct.id ? updated.data : p));
        window.dispatchEvent(new Event('productsChanged'));
      } else {
        const created = await createProduct(formData);
        setProducts([...products, created.data]);
        window.dispatchEvent(new Event('productsChanged'));
      }
      closeModal();
    } catch {
      alert('Error al guardar producto');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Seguro que quieres eliminar este producto?')) {
      try {
        await deleteProduct(id);
        setProducts(products.filter(p => p.id !== id));
        window.dispatchEvent(new Event('productsChanged'));
      } catch (err) {
        const status = err?.response?.status;
        if (status === 409) {
          // El interceptor global ya alerta el conflicto específico para productos.
          // Evitamos duplicar el mensaje aquí.
        } else if (status === 403) {
            alert('Acceso prohibido: permisos insuficientes para eliminar productos.');
        } else {
            alert('Error al eliminar producto');
        }
      }
    }
  };

  if (loading) return <div>Cargando productos...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="admin-products">
      <h1>Gestión de Productos</h1>
      <div className="controls">        <button onClick={(() => navigate('/admin'))} className="add-btn">Volver</button>
        <input
          type="text"
          placeholder="Buscar por nombre..."
          value={searchTerm}
          onChange={handleSearch}
          className="search-input"
        />
        <button onClick={toggleStockFilter} className={`filter-btn ${filterStock ? 'active' : ''}`}>
          {filterStock ? 'Mostrar Todos' : 'Stock Crítico (<5)'}
        </button>
        <button onClick={() => openModal()} className="add-btn">Agregar Producto</button>
      </div>
      <div className="table-responsive">
      <table className="admin-table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {filteredProducts.map(product => (
            <tr key={product.id}>
              <td>{product.nombre}</td>
              <td>{product.categoria}</td>
              <td>{formatPriceCOP(product.precio)}</td>
              <td>{product.stock}</td>
              <td>{product.activo ? 'Activo' : 'Inactivo'}</td>
              <td>
                <button 
                  className={`btn-toggle ${product.activo ? 'btn-danger' : 'btn-success'}`}
                  onClick={() => handleToggle(product.id, product.activo)}
                >
                  {product.activo ? 'Desactivar' : 'Activar'}
                </button>
                <button className="btn-edit" onClick={() => openModal(product)}>Editar</button>
                <button className="btn-delete" onClick={() => handleDelete(product.id)}>Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>

      {showModal && (
        <div 
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
            <h2 style={{ color: '#2c3e50', marginBottom: '1.5rem', borderBottom: '2px solid var(--primary-color)', paddingBottom: '0.5rem' }}>
              {editingProduct ? 'Editar Producto' : 'Nuevo Producto'}
            </h2>
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <label style={{ color: '#2c3e50', fontWeight: 500 }}>Nombre</label>
              <input
                name="nombre"
                value={formData.nombre}
                onChange={handleInputChange}
                placeholder="Ej: Whey Protein Isolate"
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
              <label style={{ color: '#2c3e50', fontWeight: 500 }}>Descripción</label>
              <input
                name="descripcion"
                value={formData.descripcion}
                onChange={handleInputChange}
                placeholder="Breve descripción del producto"
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
              <label style={{ color: '#2c3e50', fontWeight: 500 }}>Precio</label>
              <input
                name="precio"
                value={formData.precio}
                onChange={handleInputChange}
                type="number"
                min="0"
                step="0.01"
                placeholder="Ej: 59.90"
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
              <label style={{ color: '#2c3e50', fontWeight: 500 }}>Stock</label>
              <input
                name="stock"
                value={formData.stock}
                onChange={handleInputChange}
                type="number"
                min="0"
                step="1"
                placeholder="Unidades disponibles (ej: 25)"
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
              <label style={{ color: '#2c3e50', fontWeight: 500 }}>Categoría</label>
              <select
                name="categoria"
                value={formData.categoria}
                onChange={handleInputChange}
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
              >
                <option value="">Selecciona categoría</option>
                {CATEGORIES.map(c => (
                  <option key={c.value} value={c.value}>{c.label}</option>
                ))}
              </select>
              <label style={{ color: '#2c3e50', fontWeight: 500 }}>Imagen (URL)</label>
              <input
                name="imagen"
                value={formData.imagen}
                onChange={handleImagenChange}
                type="url"
                placeholder="https://..."
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
              {/* Vista previa de la imagen para ayudar a verificar que la URL es la correcta */}
              <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginTop: '0.5rem' }}>
                <div style={{ flex: '0 0 120px', height: '80px', border: '1px solid #eee', borderRadius: '6px', overflow: 'hidden', display: 'flex', alignItems: 'center', justifyContent: 'center', background: '#fafafa' }}>
                  <img
                    src={formData.imagen || 'https://images.unsplash.com/photo-1514996937319-344454492b37?w=200&q=60&auto=format&fit=crop'}
                    alt="preview"
                    onLoad={() => setImageLoadOk(true)}
                    onError={(e) => { setImageLoadOk(false); e.currentTarget.src = 'https://images.unsplash.com/photo-1514996937319-344454492b37?w=200&q=60&auto=format&fit=crop'; }}
                    style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                  />
                </div>
                <div style={{ fontSize: '0.9rem', color: '#666' }}>
                  {formData.imagen ? (imageLoadOk ? 'Preview de la URL ingresada' : 'La URL no carga correctamente') : 'No hay URL - se mostrará imagen por defecto'}
                </div>
              </div>
              <button 
                type="submit"
                style={{
                  padding: '0.75rem 1.5rem',
                  backgroundColor: 'var(--primary-color)',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  fontSize: '1rem',
                  cursor: 'pointer',
                  transition: 'all 0.3s',
                  fontWeight: 500
                }}
                onMouseEnter={(e) => e.target.style.backgroundColor = 'var(--primary-color-2)'}
                onMouseLeave={(e) => e.target.style.backgroundColor = 'var(--primary-color)'}
                disabled={!!formData.imagen && !imageLoadOk}
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

export default ProductosAdmin;



