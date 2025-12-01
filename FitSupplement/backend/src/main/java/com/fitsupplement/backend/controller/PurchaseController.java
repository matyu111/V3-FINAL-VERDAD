package com.fitsupplement.backend.controller;

import com.fitsupplement.backend.entity.Purchase;
import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.service.PurchaseService;
import com.fitsupplement.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        Optional<Purchase> purchase = purchaseService.getPurchaseById(id);
        return purchase.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Purchase createPurchase(
            @RequestParam(required = false) Long userId,
            @RequestParam Long productId,
            @RequestParam int cantidad,
            Authentication authentication
    ) {
        Long resolvedUserId = userId;
        if (resolvedUserId == null) {
            String email = authentication != null ? authentication.getName() : null;
            if (email == null || email.isBlank()) {
                throw new RuntimeException("Usuario no autenticado");
            }
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            resolvedUserId = user.getId();
        }
        return purchaseService.createPurchase(resolvedUserId, productId, cantidad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<Purchase> getPurchasesByUser(@PathVariable Long userId) {
        return purchaseService.getPurchasesByUser(userId);
    }

    @GetMapping("/product/{productId}")
    public List<Purchase> getPurchasesByProduct(@PathVariable Long productId) {
        return purchaseService.getPurchasesByProduct(productId);
    }

    // Endpoints para estadísticas del dashboard (acceso restringido a admin más adelante)
    @GetMapping("/stats/total-purchases")
    public long getTotalPurchases() {
        return purchaseService.getTotalPurchases();
    }

    @GetMapping("/stats/total-revenue")
    public Double getTotalRevenue() {
        return purchaseService.getTotalRevenue();
    }
}