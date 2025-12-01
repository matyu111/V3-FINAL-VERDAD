import axios from "axios";

const api = axios.create({
  // En desarrollo, usa el backend por defecto si no hay VITE_API_URL
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8081",
});

// Adjunta el JWT si existe en localStorage
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers = config.headers || {};
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

// Manejo global de errores: cerrar sesión SOLO en 401.
// En 403 mostrar mensaje de permisos sin cerrar sesión.
// Evitar redirección cuando el fallo ocurre en /auth/login o ya estamos en /login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    const url = error?.config?.url || '';
    const method = (error?.config?.method || '').toLowerCase();
    const isAuthLogin = url.includes('/auth/login');
    const isOnLoginPage = typeof window !== 'undefined' && window.location?.pathname === '/login';
    const serverMsg = (error?.response?.data?.error || '').toLowerCase();
    // Cerrar sesión si el token es inválido/expirado (401)
    if (status === 401 && !isAuthLogin) {
      try {
        localStorage.removeItem("token");
        localStorage.removeItem("currentUser");
      } catch {}
      if (typeof window !== "undefined" && !isOnLoginPage) {
        try {
          alert("Tu sesión no es válida o ha expirado. Inicia sesión nuevamente.");
        } catch {}
        window.location.href = "/login";
      }
    }
    // En 403 no cerramos sesión, sólo informamos si aplica
    else if (status === 403 && !isAuthLogin) {
      // Si el backend indica cuenta inactiva, tratamos como 401
      if (serverMsg.includes("inactiva")) {
        try {
          localStorage.removeItem("token");
          localStorage.removeItem("currentUser");
        } catch {}
        if (typeof window !== "undefined" && !isOnLoginPage) {
          try { alert("Tu cuenta ha sido desactivada por un administrador."); } catch {}
          window.location.href = "/login";
        }
      } else {
        // Permisos insuficientes: dejamos la sesión activa
        try { console.warn("Acceso prohibido (403): permisos insuficientes para", url); } catch {}
      }
    }
    // Conflicto lógico (409): por ejemplo, eliminar producto con compras asociadas
    else if (status === 409) {
      const isProductDelete = method === 'delete' && (url.includes('/api/admin/products') || url.includes('/api/products'));
      const serverMsg = error?.response?.data?.error;
      const defaultMsg = isProductDelete ? 'No se puede eliminar el producto: existen compras asociadas.' : 'Conflicto: la operación no puede realizarse.';
      const msg = (typeof serverMsg === 'string' && serverMsg.trim().length > 0) ? serverMsg : defaultMsg;
      if (isProductDelete) {
        try { alert(msg); } catch {}
      } else {
        try { console.warn('Error 409 en', url, '-', msg); } catch {}
      }
    }
    return Promise.reject(error);
  }
);
export default api;




