export const formatPrice = (v) =>
  Number(v || 0).toLocaleString("es-CL", { style: "currency", currency: "CLP" });
