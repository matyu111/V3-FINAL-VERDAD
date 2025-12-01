import api from "./api";

export const getAllPurchases = () => api.get("/api/purchases");

export const getPurchaseById = (id) => api.get(`/api/purchases/${id}`);

export const createPurchase = (userId, productId, cantidad) => {
  const params = { productId, cantidad };
  if (userId) params.userId = userId;
  return api.post("/api/purchases", null, { params });
};

export const deletePurchase = (id) => api.delete(`/api/purchases/${id}`);

export const getPurchasesByUser = (userId) => api.get(`/api/purchases/user/${userId}`);

export const getPurchasesByProduct = (productId) => api.get(`/api/purchases/product/${productId}`);

export const getTotalPurchases = () => api.get("/api/purchases/stats/total-purchases");

export const getTotalRevenue = () => api.get("/api/purchases/stats/total-revenue");