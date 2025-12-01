import Header from "./Layout/Header";
import Footer from "./Layout/Footer";
import { Outlet } from "react-router-dom";

export default function Layout() {
  return (
    <>
      <Header />
      <main className="container my-4">
        <Outlet />
      </main>
      <Footer />
    </>
  );
}