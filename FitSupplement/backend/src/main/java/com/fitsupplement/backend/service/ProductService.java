package com.fitsupplement.backend.service;

import com.fitsupplement.backend.entity.Product;
import com.fitsupplement.backend.repository.ProductRepository;
import com.fitsupplement.backend.repository.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    /*
     * Servicio de negocio para productos.
     * Responsable de:
     * - Reglas de negocio (p. ej. impedir borrado de placeholder)
     * - Transformaciones ligeras antes de persistir
     * - Reasignación de relaciones (compras) cuando se elimina un producto
     */

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        Objects.requireNonNull(product, "product no puede ser null");
        return productRepository.save(product);
    }

    public Product updateProduct(long id, Product productDetails) {
        Objects.requireNonNull(productDetails, "productDetails no puede ser null");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (productDetails.getNombre() != null) {
            product.setNombre(productDetails.getNombre());
        }
        if (productDetails.getDescripcion() != null) {
            product.setDescripcion(productDetails.getDescripcion());
        }
        if (productDetails.getPrecio() != null) {
            product.setPrecio(productDetails.getPrecio());
        }
        if (productDetails.getStock() != null) {
            product.setStock(productDetails.getStock());
        }
        if (productDetails.getCategoria() != null) {
            product.setCategoria(productDetails.getCategoria());
        }
        if (productDetails.getImagen() != null) {
            product.setImagen(productDetails.getImagen());
        }

        return productRepository.save(product);
    }

    public void deleteProduct(long id) {
        // Bloquear eliminación del producto placeholder del sistema
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if ("PLACEHOLDER".equalsIgnoreCase(product.getCategoria()) ||
                (product.getNombre() != null && product.getNombre().equalsIgnoreCase("Producto eliminado"))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar el producto placeholder del sistema.");
        }

        // Reasignar compras al producto placeholder en vez de borrarlas
        Product placeholder = getOrCreateDeletedProductPlaceholder();

        List<com.fitsupplement.backend.entity.Purchase> purchases = purchaseRepository.findByProductId(id);
        for (com.fitsupplement.backend.entity.Purchase p : purchases) {
            p.setProduct(placeholder);
            purchaseRepository.save(p);
        }

        productRepository.deleteById(id);
    }

    public List<Product> findByCategoria(String categoria) {
        Objects.requireNonNull(categoria, "categoria no puede ser null");
        return productRepository.findByCategoria(categoria);
    }

    public List<Product> searchByNombre(String nombre) {
        Objects.requireNonNull(nombre, "nombre no puede ser null");
        return productRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Product toggleActive(long id) {
        Product product = getProductById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if ("PLACEHOLDER".equalsIgnoreCase(product.getCategoria()) ||
                (product.getNombre() != null && product.getNombre().equalsIgnoreCase("Producto eliminado"))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede cambiar el estado del producto placeholder del sistema.");
        }
        product.setActivo(!product.isActivo());
        return productRepository.save(product);
    }

    public long getTotalProducts() {
        // Excluir del conteo productos placeholder
        return productRepository.countByCategoriaNot("PLACEHOLDER");
    }

    private Product getOrCreateDeletedProductPlaceholder() {
        // Usar categoria "PLACEHOLDER" para identificar el producto placeholder
        List<Product> placeholders = productRepository.findByCategoria("PLACEHOLDER");
        if (!placeholders.isEmpty()) {
            Product phExisting = placeholders.get(0);
            // Asegurar que el placeholder siempre esté inactivo
            if (phExisting.isActivo()) {
                phExisting.setActivo(false);
                productRepository.save(phExisting);
            }
            return phExisting;
        }
        Product ph = new Product();
        ph.setNombre("Producto eliminado");
        ph.setDescripcion("Placeholder para productos eliminados");
        ph.setPrecio(0.0);
        ph.setStock(0);
        ph.setCategoria("PLACEHOLDER");
        ph.setActivo(false);
        ph.setImagen(null);
        return productRepository.save(ph);
    }
}