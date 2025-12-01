package com.fitsupplement.backend.controller;

import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.entity.Product;
import com.fitsupplement.backend.service.UserService;
import com.fitsupplement.backend.service.ProductService;
import com.fitsupplement.backend.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        Long totalUsers = userService.getTotalUsers();
        Long totalProducts = productService.getTotalProducts();
        Long totalPurchases = purchaseService.getTotalPurchases();
        Double totalRevenue = purchaseService.getTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }
        return Map.of(
            "totalUsers", totalUsers,
            "totalProducts", totalProducts,
            "totalPurchases", totalPurchases,
            "totalRevenue", totalRevenue
        );
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/users/{id}/toggle")
    public ResponseEntity<User> toggleUserActive(@PathVariable Long id) {
        User updatedUser = userService.toggleActive(id);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PatchMapping("/products/{id}/toggle")
    public ResponseEntity<Product> toggleProductActive(@PathVariable Long id) {
        Product updatedProduct = productService.toggleActive(id);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProductAdmin(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            String reason = ex.getReason() != null ? ex.getReason() : "Conflicto: la operación no puede realizarse.";
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", reason));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserAdmin(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            String reason = ex.getReason() != null ? ex.getReason() : "Conflicto: la operación no puede realizarse.";
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", reason));
        }
    }
}