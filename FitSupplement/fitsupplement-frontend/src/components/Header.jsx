import React from "react";\nimport { NavLink, useNavigate } from "react-router-dom";
import "./Layout/Header.css";
import { useAuth } from "../context/AuthContext.jsx";

export default function Header() {\n  React.useEffect(() => { if (isAuthenticated && typeof refreshProfile === "function") { refreshProfile(); } }, [isAuthenticated]);
  const navigate = useNavigate();
  const { user, logout, isAuthenticated, refreshProfile } = useAuth();
  return (
    <nav className="navbar navbar-expand-lg bg-white border-bottom">
      <div className="container">
        <NavLink className="navbar-brand fw-bold text-primary" to="/">NeuroVision Networks</NavLink>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuNav">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div id="menuNav" className="collapse navbar-collapse">
          <ul className="navbar-nav ms-auto">
            <li className="nav-item"><NavLink className="nav-link" to="/productos">Productos</NavLink></li>
            <li className="nav-item"><NavLink className="nav-link" to="/categorias">CategorÃ­as</NavLink></li>
            <li className="nav-item"><NavLink className="nav-link" to="/blog">Blog</NavLink></li>
            <li className="nav-item"><NavLink className="nav-link" to="/contacto">Contacto</NavLink></li>
            <li className="nav-item"><NavLink className="nav-link" to="/sobre-nosotros">Nosotros</NavLink></li>
            <li className="nav-item"><NavLink className="nav-link" to="/carrito">Carrito</NavLink></li>
            {isAuthenticated ? (
              <>
                <li className="nav-item">
                  <span className="nav-link">Bienvenido {user?.nombre}</span>
                </li>
                <li className="nav-item">
                  <button
                    className="logout-btn"
                    onClick={() => { logout(); navigate('/'); }}
                  >Cerrar sesiÃ³n</button>
                </li>
              </>
            ) : (
              <li className="nav-item"><NavLink className="nav-link" to="/login">Ingresar</NavLink></li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}

