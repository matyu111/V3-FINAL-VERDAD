# Tienda de Suplementos

Aplicación Android para la gestión y venta de suplementos deportivos, con soporte para roles de usuario (cliente y administrador).

## 📱 Características

*   **Autenticación:** Inicio de sesión seguro utilizando JWT.
*   **Catálogo de Productos:** Visualización de productos con detalles e imágenes.
*   **Carrito de Compras:** Gestión de pedidos y proceso de checkout.
*   **Panel de Administrador:**
    *   Gestión de productos (Crear, Editar, Eliminar).
    *   Gestión de usuarios.
    *   Revisión de órdenes.
*   **Perfil de Usuario:** Visualización y edición de datos personales, historial de pedidos.

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Kotlin
*   **Arquitectura:** MVVM (Model-View-ViewModel)
*   **UI:** XML Layouts, Fragments, Navigation Component
*   **Networking:** Retrofit 2, OkHttp 3
*   **Serialización:** GSON
*   **Imágenes:** Coil
*   **Inyección de Dependencias:** Manual (Singleton Pattern)
*   **Asincronía:** Kotlin Coroutines
*   **Backend:** Xano (API REST)

## 🚀 Cómo iniciar la aplicación

### Prerrequisitos

*   Android Studio Iguana o superior.
*   JDK 17.
*   Dispositivo Android o Emulador con API 26 o superior.

### Pasos para ejecutar

1.  **Clonar el repositorio:**
    ```bash
    git clone <url-del-repositorio>
    ```

2.  **Abrir en Android Studio:**
    *   Selecciona `File` > `Open` y navega hasta la carpeta raíz del proyecto (`XANO`).

3.  **Sincronizar Gradle:**
    *   Espera a que Android Studio descargue las dependencias y sincronice el proyecto.

4.  **Configurar la API (Opcional si ya está configurada):**
    *   La configuración base de la API se encuentra en `com.tiendasuplementos.app.data.remote.RetrofitClient`.
    *   Asegúrate de que las URLs base (`AUTH_URL` y `STORE_URL`) sean correctas y accesibles.

5.  **Ejecutar la App:**
    *   Conecta tu dispositivo o inicia un emulador.
    *   Haz clic en el botón "Run" (triángulo verde) en la barra de herramientas.

## 🔐 Credenciales de Prueba

Si dispones de credenciales de prueba para el administrador o usuario, puedes utilizarlas en la pantalla de Login.

*   **Usuario Admin:** (Si aplica, indicar aquí o en documentación privada)
*   **Usuario Cliente:** (Si aplica, indicar aquí o en documentación privada)

## 📁 Estructura del Proyecto

*   `data`: Capa de datos (Modelos, DTOs, Repositorios, API).
*   `ui`: Capa de presentación (Activities, Fragments, ViewModels, Adapters).
*   `util`: Clases de utilidad y constantes (SessionManager, Constantes).

## ⚠️ Notas Importantes

*   La aplicación requiere conexión a internet para funcionar correctamente.
*   El archivo `app_logo.png` debe estar presente en `app/src/main/res/drawable/` para que el icono de la aplicación se muestre correctamente.
