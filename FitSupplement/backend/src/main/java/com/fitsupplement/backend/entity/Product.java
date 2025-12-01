package com.fitsupplement.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
    /*
     * Identificador primario: generado automáticamente por la base de datos.
     * Uso: clave única para referencias y DTOs.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Campos del producto (datos esenciales mostrados en el frontend):
     * - nombre: título del producto (no nulo)
     * - descripcion: texto opcional con detalles
     * - precio: valor numérico (no nulo)
     * - stock: cantidad disponible (no nulo)
     * - categoria: etiqueta para filtros y navegación (no nulo)
     * - activo: flag para soft-enable/disable del producto en la tienda
     * - imagen: URL o ruta a la imagen que mostrará el producto
     */
    @Column(nullable = false)
    private String nombre;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private boolean activo = true;

    @Column
    private String imagen;

    public String getNombre() {
        return nombre;
    }

    /*
     * Getters/Setters: aunque Lombok @Data genera estos métodos automáticamente,
     * se mantienen explícitos aquí para claridad y para que los ejemplos del
     * video puedan mostrarlos directamente.
     */

    public String getDescripcion() {
        return descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public Integer getStock() {
        return stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}