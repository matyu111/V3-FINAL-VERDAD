package com.fitsupplement.backend.controller;

import com.fitsupplement.backend.entity.Product;
import com.fitsupplement.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "API para gestión de productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    /*
     * Controlador REST expone endpoints bajo /api/products.
     * - El frontend consume estos endpoints (GET/POST/PUT/DELETE) para
     * mostrar listados, crear y actualizar productos.
     * - Las respuestas son JSON y los códigos HTTP reflejan el resultado.
     */

    @GetMapping
    @Operation(summary = "Obtener todos los productos")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            String reason = ex.getReason() != null ? ex.getReason() : "Conflicto: la operación no puede realizarse.";
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", reason));
        }
    }

    @GetMapping("/category/{categoria}")
    @Operation(summary = "Buscar productos por categoría")
    public List<Product> getProductsByCategory(@PathVariable String categoria) {
        return productService.findByCategoria(categoria);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar productos por nombre")
    public List<Product> searchProducts(@RequestParam String nombre) {
        return productService.searchByNombre(nombre);
    }
}