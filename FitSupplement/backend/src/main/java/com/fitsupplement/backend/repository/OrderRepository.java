package com.fitsupplement.backend.repository;

import com.fitsupplement.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query("SELECT SUM(o.total) FROM Order o")
    Double sumTotalRevenue();
}
