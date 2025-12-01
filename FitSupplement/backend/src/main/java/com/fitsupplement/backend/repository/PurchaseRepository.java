package com.fitsupplement.backend.repository;

import com.fitsupplement.backend.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByUserId(Long userId);
    
    List<Purchase> findByProductId(Long productId);

    long countByProductId(Long productId);
    long countByUserId(Long userId);
    
    @Query("SELECT COUNT(DISTINCT p.user.id) FROM Purchase p")
    long countDistinctUsers();
    
    @Query("SELECT COUNT(p) FROM Purchase p")
    long countTotalPurchases();
    
    @Query("SELECT SUM(p.precioTotal) FROM Purchase p")
    Double sumTotalRevenue();
    
    List<Purchase> findByFechaCompraBetween(LocalDateTime start, LocalDateTime end);
}