import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { getDashboardStats } from '../../services/adminService';
import '../../styles/Dashboard.css'; // Asumiendo que crearemos este archivo después

const Dashboard = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState({ totalUsers: 0, totalProducts: 0, totalPurchases: 0, totalRevenue: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchStats = useCallback(async () => {
    try {
      const response = await getDashboardStats();
      setStats({
        totalUsers: response.data.totalUsers,
        totalProducts: response.data.totalProducts,
        totalPurchases: response.data.totalPurchases,
        totalRevenue: response.data.totalRevenue
      });
    } catch (err) {
      setError('Error al cargar estadísticas');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('currentUser'));
    if (!user || user.role !== 'ADMIN') {
      navigate('/login'); // Redirigir si no es admin
      return;
    }
    fetchStats();

    const onPurchaseOrProducts = () => {
      setLoading(true);
      fetchStats();
    };
    window.addEventListener('purchaseCompleted', onPurchaseOrProducts);
    window.addEventListener('productsChanged', onPurchaseOrProducts);
    return () => {
      window.removeEventListener('purchaseCompleted', onPurchaseOrProducts);
      window.removeEventListener('productsChanged', onPurchaseOrProducts);
    };
  }, [navigate, fetchStats]);

  if (loading) return <div>Cargando...</div>;
  if (error) return <div>{error}</div>;

  const formatCOP = (value) => {
    return `$${Number(value || 0).toLocaleString('es-CO', { maximumFractionDigits: 0 })}`;
  };

  return (
    <div className="dashboard">
      <h1>Panel de Administración</h1>
      <div className="stats">
        <div className="stat-card">
          <h2>Usuarios Registrados</h2>
          <p>{stats.totalUsers}</p>
        </div>
        <div className="stat-card">
          <h2>Productos Totales</h2>
          <p>{stats.totalProducts}</p>
        </div>
        <div className="stat-card">
          <h2>Compras Totales</h2>
          <p>{stats.totalPurchases}</p>
        </div>
        <div className="stat-card">
          <h2>Ingresos Totales</h2>
          <p>{formatCOP(stats.totalRevenue)}</p>
        </div>
      </div>
      <div className="links">
        <button onClick={() => navigate('/admin/usuarios')}>Gestión de Usuarios</button>
        <button onClick={() => navigate('/admin/productos')}>Gestión de Productos</button>
        <button onClick={() => navigate('/admin/ventas')}>Gestión de Ventas</button>
      </div>
    </div>
  );
};

export default Dashboard;