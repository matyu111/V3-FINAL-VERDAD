import { createBrowserRouter } from "react-router-dom";
import Home from "./pages/Home/Home";
import Productos from "./pages/Productos/Productos";
import Categorias from "./pages/Categorias/Categorias";
import Blog from "./pages/Blog/Blog";
import ArticuloCreatina from "./pages/Blog/ArticuloCreatina";
import ArticuloProteinas from "./pages/Blog/ArticuloProteinas";
import Contacto from "./pages/Contacto/Contacto";
import SobreNosotros from "./pages/SobreNosotros/SobreNosotros";
import Carrito from "./pages/Carrito/Carrito";
import Checkout from "./pages/Checkout/Checkout";
import Login from "./pages/Auth/Login";
import Registro from "./pages/Auth/Registro";
import Perfil from "./pages/Auth/Perfil";
import Dashboard from "./pages/Admin/Dashboard";
import Usuarios from "./pages/Admin/Usuarios";
import ProductosAdmin from "./pages/Admin/ProductosAdmin";
import Ventas from "./pages/Admin/Ventas";
import ProductDetail from "./pages/ProductDetail/ProductDetail";
import Layout from "./components/Layout";

export const router = createBrowserRouter([
  {
    element: <Layout />,
    children: [
      { path: "/", element: <Home /> },
      { path: "/productos", element: <Productos /> },
      { path: "/productos/:id", element: <ProductDetail /> },
      { path: "/categorias", element: <Categorias /> },
      { path: "/blog", element: <Blog /> },
      { path: "/blog/creatina", element: <ArticuloCreatina /> },
      { path: "/blog/proteinas", element: <ArticuloProteinas /> },
      { path: "/contacto", element: <Contacto /> },
      { path: "/sobre-nosotros", element: <SobreNosotros /> },
      { path: "/carrito", element: <Carrito /> },
      { path: "/checkout", element: <Checkout /> },
      { path: "/login", element: <Login /> },
      { path: "/registro", element: <Registro /> },
      { path: "/perfil", element: <Perfil /> },
      { path: "/admin", element: <Dashboard /> },
      { path: "/admin/usuarios", element: <Usuarios /> },
      { path: "/admin/productos", element: <ProductosAdmin /> },
      { path: "/admin/ventas", element: <Ventas /> },
    ]
  }
]);
