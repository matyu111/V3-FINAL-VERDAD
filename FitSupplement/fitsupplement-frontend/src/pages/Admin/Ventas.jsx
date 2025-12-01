import React, { useState, useEffect } from 'react';
import { getAllOrders } from '../../services/cartService';
import '../../styles/Ventas.css';

const Ventas = () => {
  const [purchases, setPurchases] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [dateFilter, setDateFilter] = useState('');
  const [stats, setStats] = useState({ totalPurchases: 0, totalRevenue: 0 });
  const [selectedOrder, setSelectedOrder] = useState(null);

  useEffect(() => {
    fetchPurchases();
  }, []);

  const fetchPurchases = async () => {
    try {
      const response = await getAllOrders();
      // orders will be used as 'purchases' in this view
      setPurchases(response.data);
      // Calcular estadísticas
      const totalPurchases = response.data.length;
      const totalRevenue = response.data.reduce((sum, p) => sum + (p.total || 0), 0);
      setStats({ totalPurchases, totalRevenue });
      setLoading(false);
    } catch (err) {
      setError('Error al cargar ventas');
      setLoading(false);
    }
  };

  const filteredPurchases = purchases.filter(purchase => {
    const matchesUser = purchase.user?.nombre?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesDate = !dateFilter || (purchase.fechaCompra && purchase.fechaCompra.startsWith(dateFilter));
    return matchesUser && matchesDate;
  });

  if (loading) return <div>Cargando ventas...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="admin-sales">
      <h1>Gestión de Ventas</h1>
      <div className="stats">
        <div className="stat-card">
          <h2>Total Ventas</h2>
          <p>{stats.totalPurchases}</p>
        </div>
        <div className="stat-card">
          <h2>Ingresos Totales</h2>
          <p>${stats.totalRevenue.toFixed(2)}</p>
        </div>
      </div>
      <div className="controls">
        <input
          type="text"
          placeholder="Buscar por usuario..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
        <input
          type="date"
          value={dateFilter}
          onChange={(e) => setDateFilter(e.target.value)}
          className="date-filter"
        />
      </div>
      <table className="admin-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Usuario</th>
            <th>Productos (cant.)</th>
            <th>Total</th>
            <th>Acción</th>
            <th>Fecha</th>
          </tr>
        </thead>
        <tbody>
          {filteredPurchases.map(purchase => (
            <tr key={purchase.id}>
              <td>{purchase.id}</td>
              <td>{purchase.user.nombre}</td>
              <td>
                {Array.isArray(purchase.items) ? (
                  purchase.items.map((it, i) => (
                    <div key={i}>{it.product?.nombre || it.productId} ({it.cantidad})</div>
                  ))
                ) : null}
              </td>
              <td>${(purchase.total || 0).toFixed(2)}</td>
              <td>
                <button className="btn btn-sm" onClick={() => setSelectedOrder(purchase)}>Ver Boleta</button>
              </td>
              <td>{new Date(purchase.fechaCompra).toLocaleDateString()}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedOrder && (
        <div className="modal-overlay" style={{position:'fixed',inset:0,background:'rgba(0,0,0,0.4)',display:'flex',alignItems:'center',justifyContent:'center'}}>
          <div style={{background:'#fff',padding:20,borderRadius:8,maxWidth:'90%',width:700}}>
            <h3>Boleta - Orden #{selectedOrder.id}</h3>
            <p>Usuario: {selectedOrder.user?.nombre}</p>
            <div style={{marginBottom:12}}>
              {Array.isArray(selectedOrder.items) ? selectedOrder.items.map((it, idx) => (
                <div key={idx} style={{display:'flex',justifyContent:'space-between',padding:'4px 0'}}>
                  <div>{it.product?.nombre || it.productId} x{it.cantidad}</div>
                  <div>${(it.precioTotal || 0).toFixed(2)}</div>
                </div>
              )) : null}
            </div>
            <div style={{borderTop:'1px solid #eee',paddingTop:12}}>
              <div style={{display:'flex',justifyContent:'space-between'}}><div>Subtotal</div><div>${(selectedOrder.subtotal || 0).toFixed(2)}</div></div>
              <div style={{display:'flex',justifyContent:'space-between'}}><div>IVA 19%</div><div>${(selectedOrder.tax || 0).toFixed(2)}</div></div>
              <div style={{display:'flex',justifyContent:'space-between',fontWeight:'bold'}}><div>Total</div><div>${(selectedOrder.total || 0).toFixed(2)}</div></div>
            </div>
            <div style={{display:'flex',justifyContent:'flex-end',marginTop:12}}>
              <button className="btn btn-secondary" onClick={() => setSelectedOrder(null)}>Cerrar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Ventas;