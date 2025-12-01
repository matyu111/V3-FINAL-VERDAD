import { createContext, useContext, useEffect, useState } from "react";
import { login as apiLogin, register as apiRegister, getProfile } from "../services/authService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("currentUser")) || null;
    } catch {
      return null;
    }
  });

  useEffect(() => {
    // Sincroniza cambios del usuario con localStorage
    if (user) {
      localStorage.setItem("currentUser", JSON.stringify(user));
    } else {
      localStorage.removeItem("currentUser");
    }
  }, [user]);

  const login = async (email, password) => {
    try {
      const { data } = await apiLogin(email, password);
      if (data?.jwt && data?.user) {
        localStorage.setItem("token", data.jwt);
        setUser(data.user);
        return { ok: true, message: "Inicio de sesión correcto" };
      }
      return { ok: false, message: "Respuesta inesperada" };
    } catch (err) {
      const status = err?.response?.status;
      const serverMsg = err?.response?.data?.error;
      const msg = (status === 401 || status === 404)
        ? "Correo o contraseña inválidos"
        : (serverMsg || "Error al iniciar sesión");
      return { ok: false, message: msg };
    }
  };

  const register = async (payload) => {
    try {
      const { data } = await apiRegister(payload);
      // Soportar ambas respuestas: antigua (datos de usuario) y nueva ({ jwt, user })
      if (data?.jwt && data?.user) {
        localStorage.setItem("token", data.jwt);
        setUser(data.user);
        return { ok: true, data: data.user, message: "Registro exitoso" };
      } else {
        setUser(data);
        return { ok: true, data, message: "Registro exitoso" };
      }
    } catch (err) {
      const msg = err?.response?.data?.error || "Error al registrarse";
      return { ok: false, message: msg };
    }
  };

  const refreshProfile = async () => {
    try {
      const { data } = await getProfile();
      setUser(data);
      return { ok: true, data };
    } catch {
      return { ok: false };
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
  };

  // Verificación periódica: si la cuenta se desactiva, cerrar sesión y avisar
  useEffect(() => {
    if (!user) return;
    const intervalId = setInterval(async () => {
      try {
        const { data } = await getProfile();
        // Actualiza usuario por si cambió algún dato
        setUser(data);
        if (!data?.activo) {
          try { alert("Tu cuenta ha sido desactivada por un administrador. Se cerrará la sesión."); } catch {}
          logout();
          if (typeof window !== "undefined") {
            window.location.href = "/login";
          }
        }
      } catch (err) {
        const status = err?.response?.status;
        // Sólo cerramos sesión en 401 (token inválido/expirado)
        if (status === 401) {
          try { alert("Tu sesión ha expirado o fue invalidada. Se cerrará la sesión."); } catch {}
          logout();
          if (typeof window !== "undefined") {
            window.location.href = "/login";
          }
        } else if (status === 403) {
          // 403 no debe provocar cierre de sesión; puede ser falta de permisos
          try { console.warn("Perfil no accesible (403). Manteniendo sesión activa."); } catch {}
        }
      }
    }, 1000);
    return () => clearInterval(intervalId);
  }, [user]);

  return (
    <AuthContext.Provider value={{ user, login, register, refreshProfile, logout, isAuthenticated: !!user, isAdmin: user?.role === "ADMIN" }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

