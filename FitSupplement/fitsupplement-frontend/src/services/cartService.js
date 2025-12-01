import api from "./api";

export const createOrder = (data) => api.post("/api/orders", data);
export const getOrdersByUser = (id) => api.get(`/api/orders/user/${id}`);
export const getAllOrders = () => api.get('/api/orders');
