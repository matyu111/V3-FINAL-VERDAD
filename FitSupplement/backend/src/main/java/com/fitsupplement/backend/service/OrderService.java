package com.fitsupplement.backend.service;

import com.fitsupplement.backend.entity.Order;
import com.fitsupplement.backend.entity.OrderItem;
import com.fitsupplement.backend.entity.Product;
import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.repository.OrderItemRepository;
import com.fitsupplement.backend.repository.OrderRepository;
import com.fitsupplement.backend.repository.ProductRepository;
import com.fitsupplement.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser(long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order createOrder(long userId, List<OrderItemRequest> itemsReq, Double shipping, Double discount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Order order = new Order();
        order.setUser(user);
        order.setFechaCompra(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        double subtotal = 0.0;

        for (OrderItemRequest req : itemsReq) {
            Product product = productRepository.findById(req.productId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + req.productId));
            if (product.getStock() < req.quantity) {
                throw new RuntimeException("Stock insuficiente para producto " + product.getId());
            }
            double unitPrice = product.getPrecio();
            double itemTotal = unitPrice * req.quantity;

            // reduce stock
            product.setStock(product.getStock() - req.quantity);
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setCantidad(req.quantity);
            item.setPrecioUnitario(unitPrice);
            item.setPrecioTotal(itemTotal);
            item.setOrder(order);
            items.add(item);

            subtotal += itemTotal;
        }

        double appliedDiscount = discount == null ? 0.0 : discount;
        double taxableBase = subtotal - appliedDiscount;
        double tax = taxableBase * 0.19; // IVA 19%
        double appliedShipping = shipping == null ? 0.0 : shipping;
        double total = taxableBase + tax + appliedShipping;

        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setTotal(total);
        order.setItems(items);

        // save order first (cascade should save items)
        Order saved = orderRepository.save(order);
        // ensure items are saved with order reference
        for (OrderItem it : items) {
            it.setOrder(saved);
            orderItemRepository.save(it);
        }

        return saved;
    }

    public static class OrderItemRequest {
        public Long productId;
        public int quantity;
    }
}
