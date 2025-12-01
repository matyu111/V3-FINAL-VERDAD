export const isEmail = (v) => /\S+@\S+\.\S+/.test(v);
export const isRequired = (v) => String(v || "").trim().length > 0;
export const isPositive = (v) => !isNaN(v) && Number(v) > 0;
