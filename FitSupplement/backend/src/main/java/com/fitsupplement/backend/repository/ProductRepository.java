package com.fitsupplement.backend.repository;

import com.fitsupplement.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /*
     * Consultas personalizadas proporcionadas por Spring Data JPA.
     * - findByCategoria: obtener productos que pertenecen a una categoría.
     * - findByNombreContainingIgnoreCase: búsqueda por texto en el nombre
     * (insensible a mayúsculas).
     * - countByCategoriaNot: contar productos excluyendo categoría placeholder
     * (útil para métricas).
     */
    List<Product> findByCategoria(String categoria);

    List<Product> findByNombreContainingIgnoreCase(String nombre);

    long countByCategoriaNot(String categoria);
}