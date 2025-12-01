package com.fitsupplement.backend.controller;

import com.fitsupplement.backend.entity.Order;
import com.fitsupplement.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{id}")
    public List<Order> getOrdersByUser(@PathVariable Long id) {
        return orderService.getOrdersByUser(id);
    }

    public static class CreateOrderRequest {
        public Long userId;
        public List<OrderItemRequest> items;
        public Double shipping;
        public Double discount;

        public static class OrderItemRequest {
            public Long productId;
            public Integer quantity;
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order saved = orderService.createOrder(request.userId, request.items.stream().map(i -> {
            OrderService.OrderItemRequest r = new OrderService.OrderItemRequest();
            r.productId = i.productId;
            r.quantity = i.quantity == null ? 1 : i.quantity;
            return r;
        }).toList(), request.shipping, request.discount);

        return ResponseEntity.ok(saved);
    }
}
