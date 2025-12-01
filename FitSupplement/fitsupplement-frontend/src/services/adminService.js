import api from "./api";

export const getDashboardStats = () => api.get("/api/admin/stats");

export const getAllUsers = () => api.get("/api/admin/users");

export const toggleUser = (id) => api.patch(`/api/admin/users/${id}/toggle`);

export const createUser = (data) => api.post("/api/users", data);

export const updateUser = (id, data) => api.put(`/api/users/${id}`, data);

// Operación administrativa protegida
export const deleteUser = (id) => api.delete(`/api/admin/users/${id}`);

export const getAllProductsAdmin = () => api.get("/api/admin/products");

export const toggleProduct = (id) => api.patch(`/api/admin/products/${id}/toggle`);
