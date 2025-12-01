# FitSupplement

Proyecto full‑stack para tienda de suplementos. Backend en Spring Boot 3.1.5, frontend en React + Vite.

## Puertos y URLs

- Backend: `http://localhost:8081`
  - Swagger UI: `http://localhost:8081/swagger-ui.html`
  - OpenAPI json: `http://localhost:8081/api-docs`
- Frontend (desarrollo): normalmente `http://localhost:5177/` (Vite puede elegir otro puerto si 5177 está ocupado).
  - El proxy de Vite redirige `/api` al backend `http://localhost:8081`.
  - El cliente `axios` usa `VITE_API_URL` o, si no está definida, `http://localhost:8081`.

## Requisitos

- Java 17 y Maven 3.8+
- Node.js 18+ y npm
- MySQL 8+ y MySQL Workbench

## Configuración de Base de Datos

1. Crea la base de datos `fitsupplement_db` en MySQL:
   ```sql
   CREATE DATABASE IF NOT EXISTS fitsupplement_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Abre `backend/src/main/resources/application.properties` y ajusta credenciales:
   - `spring.datasource.url=jdbc:mysql://localhost:3306/fitsupplement_db`
   - `spring.datasource.username=<tu_usuario>`
   - `spring.datasource.password=<tu_password>`
3. Importa el poblamiento de ejemplo desde MySQL Workbench:
   - Archivo: `backend/sql/seed_mysql.sql`
   - Este script inserta usuarios y productos con imágenes (URLs públicas).
   - El backend también crea automáticamente el usuario admin `admin@profesor.com` con contraseña `Admin123!` si no existe.

### Contraseñas y hashes

- Los usuarios del script `.sql` tienen `password` en hash BCrypt. Si quieres usar una contraseña conocida, reemplaza el hash por uno generado para tu clave.
- Ejemplo para generar un hash en Java:
  ```java
  import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
  System.out.println(new BCryptPasswordEncoder().encode("TuClaveSegura123!"));
  ```

## Cómo ejecutar (MySQL, perfil por defecto)

1. Backend:
   - En una terminal:
     ```bash
     cd FitSupplement/backend
     mvn spring-boot:run
     ```
   - Arranca en `http://localhost:8081` (perfil por defecto usa MySQL).
2. Frontend:
   - En otra terminal:
     ```bash
     cd FitSupplement/fitsupplement-frontend
     npm install
     npm run dev -- --port 5177
     ```
   - Abre `http://localhost:5177/`.

## Uso rápido

- Login de administrador (semilla automática del backend):
  - Usuario: `admin@profesor.com`
  - Contraseña: `Admin123!`

## Credenciales de prueba

Para facilitar pruebas locales añadimos dos cuentas de ejemplo (no usar en producción). Si prefieres, puedes importar/editar `backend/sql/seed_mysql.sql` para cambiar los usuarios o sus hashes.

- **Administrador**

  - Usuario (email): `admin@local.test`
  - Contraseña: `A8!vR9u$kQ2z` (ejemplo de contraseña segura)
  - Rol: `ROLE_ADMIN`

- **Usuario normal**
  - Usuario (email): `user@local.test`
  - Contraseña: `P4s$w0rd!x7Gm` (ejemplo de contraseña segura)
  - Rol: `ROLE_USER`

## Notas de seguridad

- Estos credenciales son para pruebas locales únicamente. En entornos reales, reemplázalos por contraseñas generadas y seguras.
- El backend almacena contraseñas con BCrypt. Para generar un hash BCrypt desde Java:

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
System.out.println(new BCryptPasswordEncoder().encode("TuClaveSegura123!"));
```

- Si usas el script `backend/sql/seed_mysql.sql`, reemplaza los hashes de ejemplo por hashes generados con la contraseña que elijas.
- Endpoints principales:
  - `GET /api/products` — lista de productos
  - `GET /api/products/{id}` — detalle
  - `GET /api/products/category/{categoria}` — por categoría
  - `POST /api/auth/login` — autenticación JWT

## Perfil de desarrollo (H2)

- Opcionalmente puedes ejecutar con H2 en memoria:
  ```bash
  mvn spring-boot:run -Dspring-boot.run.profiles=dev
  ```
  - Puerto: `8081`
  - Consola H2: `http://localhost:8081/h2-console`

## Notas

- Si alguna imagen externa falla, el frontend usa un fallback por categoría/ID a assets locales.
- Swagger/OpenAPI están habilitados y accesibles sin autenticación.
- Asegúrate que MySQL esté corriendo antes de iniciar el backend.
