package com.fitsupplement.backend.service;

import com.fitsupplement.backend.entity.Purchase;
import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.entity.Product;
import com.fitsupplement.backend.repository.PurchaseRepository;
import com.fitsupplement.backend.repository.UserRepository;
import com.fitsupplement.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private com.fitsupplement.backend.repository.OrderRepository orderRepository;

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Optional<Purchase> getPurchaseById(long id) {
        return purchaseRepository.findById(id);
    }

    public Purchase createPurchase(long userId, long productId, int cantidad) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setProduct(product);
        purchase.setCantidad(cantidad);
        purchase.setPrecioTotal(product.getPrecio() * cantidad);
        purchase.setFechaCompra(LocalDateTime.now());

        // Actualizar stock del producto
        product.setStock(product.getStock() - cantidad);
        productRepository.save(product);

        return purchaseRepository.save(purchase);
    }

    public void deletePurchase(long id) {
        purchaseRepository.deleteById(id);
    }

    public List<Purchase> getPurchasesByUser(long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    public List<Purchase> getPurchasesByProduct(long productId) {
        return purchaseRepository.findByProductId(productId);
    }

    // Métodos para estadísticas del dashboard
    public long getTotalUsersWithPurchases() {
        return purchaseRepository.countDistinctUsers();
    }

    public long getTotalPurchases() {
        return purchaseRepository.countTotalPurchases();
    }

    public Double getTotalRevenue() {
        Double purchasesSum = purchaseRepository.sumTotalRevenue();
        Double ordersSum = null;
        try {
            ordersSum = orderRepository.sumTotalRevenue();
        } catch (Exception e) {
            // in case orders table or repository is not present in older versions, ignore
            ordersSum = null;
        }

        double total = 0.0;
        if (purchasesSum != null)
            total += purchasesSum;
        if (ordersSum != null)
            total += ordersSum;

        // Return 0.0 if no data present to make frontend logic simpler
        return total;
    }
}