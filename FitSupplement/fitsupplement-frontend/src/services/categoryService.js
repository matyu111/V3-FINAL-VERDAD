import api from "./api";

export const getAllCategories = () => api.get("/api/categorias");
