-- FitSupplement: Poblamiento inicial para MySQL Workbench
-- Base de datos objetivo: fitsupplement_db
-- Ejecuta este script completo en MySQL Workbench conectado a tu instancia local.

USE fitsupplement_db;

-- =============================================
-- USUARIOS
-- Nota: los passwords están en hash BCrypt.
-- Si quieres usar una clave conocida, reemplaza el campo `password`
-- por el hash BCrypt de tu clave. Ejemplo en Java:
--   new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("TuClave")
-- El backend ya crea automáticamente el admin `admin@profesor.com` (Admin123!).
-- Estos usuarios de ejemplo usan correos distintos para evitar conflicto.
-- - Admin: admin@duoc.cl / Admin123!
-- Usuario Gmail: usuario@gmail.com / User123!
--Profesor Duoc: profesor@profesor.duoc.cl / Profesor123!
-- =============================================

INSERT INTO users (nombre, apellido, email, telefono, fechaNacimiento, direccion, password, objetivos, aceptoMarketing, role, activo)
VALUES
('Camila', 'García', 'camila@fitsupplement.local', '3001234567', '1995-04-12', 'Cra 10 # 20-30, Bogotá', '$2a$10$Zxq2hD3b0Q1lJqB8WJrTNOm4Y5xYHgyx1vG1tYxwE5JQ1gK0H7v9S', 'Perder grasa y tonificar', TRUE, 'USER', TRUE),
('Diego', 'Pérez', 'diego@fitsupplement.local', '3119876543', '1990-09-22', 'Av. Siempre Viva 742, Medellín', '$2a$10$Y1rZtP72aF1r9j1pB0qkWe0oG6lX8yGq8q3yYxS4F8jJrPqKZqL2e', 'Ganar masa muscular', FALSE, 'USER', TRUE),
('Sofía', 'Rojas', 'sofia@fitsupplement.local', '3025557788', '1998-01-30', 'Cll 50 # 30-10, Cali', '$2a$10$H3dPqL71kO9yTuG1rB2fXe5zP6pYqT8nL0mS5xV3C9eUxR2hJkP7a', 'Rendimiento deportivo', TRUE, 'ADMIN', TRUE);

-- =============================================
-- PRODUCTOS (con imágenes)
-- Columnas: nombre, descripcion, precio, stock, categoria, activo, imagen
-- Categorías válidas: proteinas, creatina, pre-entreno, vitaminas, aminoacidos, quemadores
-- =============================================

INSERT INTO products (nombre, descripcion, precio, stock, categoria, activo, imagen)
VALUES
('Proteína Whey Premium', 'Proteína de suero de alta calidad para recuperación muscular óptima.', 29990.00, 50, 'proteinas', TRUE, 'https://images.unsplash.com/photo-1588373983581-1f578f5db026?q=80&w=1080&auto=format&fit=crop'),
('Proteína Caseína', 'Absorción lenta ideal para tomarse antes de dormir.', 25990.00, 40, 'proteinas', TRUE, 'https://images.unsplash.com/photo-1600959907703-18a4b9bd0f7e?q=80&w=1080&auto=format&fit=crop'),
('Creatina Monohidratada', 'Creatina pura para fuerza y potencia muscular.', 19990.00, 30, 'creatina', TRUE, 'https://images.unsplash.com/photo-1615484477689-51d64bd08629?q=80&w=1080&auto=format&fit=crop'),
('Creatina HCL', 'Forma más soluble de creatina para mejor absorción.', 22990.00, 25, 'creatina', TRUE, 'https://images.unsplash.com/photo-1625662174865-2e3e5a832fa7?q=80&w=1080&auto=format&fit=crop'),
('Pre-entreno Explosivo', 'Energía y enfoque para entrenamientos intensos.', 24990.00, 35, 'pre-entreno', TRUE, 'https://images.unsplash.com/photo-1579154204601-0516acc6500b?q=80&w=1080&auto=format&fit=crop'),
('Pre-entreno Natural', 'Energía natural sin estimulantes artificiales.', 19990.00, 20, 'pre-entreno', TRUE, 'https://images.unsplash.com/photo-1515879218367-8466d910aaa3?q=80&w=1080&auto=format&fit=crop'),
('Multivitamínico Completo', 'Vitaminas y minerales esenciales en una cápsula.', 15990.00, 60, 'vitaminas', TRUE, 'https://images.unsplash.com/photo-1576092762801-3f13c8f50e88?q=80&w=1080&auto=format&fit=crop'),
('Vitamina D3 + K2', 'Apoyo para huesos y sistema inmune.', 12990.00, 45, 'vitaminas', TRUE, 'https://images.unsplash.com/photo-1601600576337-76c8b9a04b36?q=80&w=1080&auto=format&fit=crop'),
('BCAA 2:1:1', 'Aminoácidos esenciales para recuperación muscular.', 14990.00, 55, 'aminoacidos', TRUE, 'https://images.unsplash.com/photo-1524594224038-7041fc48f3f3?q=80&w=1080&auto=format&fit=crop'),
('Quemador de Grasa Avanzado', 'Termogénico para apoyo en pérdida de grasa.', 21990.00, 28, 'quemadores', TRUE, 'https://images.unsplash.com/photo-1515879218367-8466d910aaa3?q=80&w=1080&auto=format&fit=crop');

-- Sugerencia: si alguna URL de imagen falla, el frontend aplica un fallback
-- basado en categoría/ID a assets locales (producto1..producto7).

-- Fin del script