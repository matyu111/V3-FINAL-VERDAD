package com.fitsupplement.backend.service;

import com.fitsupplement.backend.entity.Product;
import com.fitsupplement.backend.entity.Purchase;
import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.repository.ProductRepository;
import com.fitsupplement.backend.repository.PurchaseRepository;
import com.fitsupplement.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    PurchaseRepository purchaseRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    com.fitsupplement.backend.repository.OrderRepository orderRepository;

    @InjectMocks
    PurchaseService purchaseService;

    @Test
    void createPurchase_updatesStockAndSaves() {
        User u = new User();
        u.setId(3L);
        Product prod = new Product();
        prod.setId(5L);
        prod.setPrecio(29990.0);
        prod.setStock(50);

        when(userRepository.findById(3L)).thenReturn(Optional.of(u));
        when(productRepository.findById(5L)).thenReturn(Optional.of(prod));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(inv -> inv.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Purchase p = purchaseService.createPurchase(3L, 5L, 2);
        assertEquals(29990.0 * 2, p.getPrecioTotal());
        assertEquals(48, prod.getStock());
        verify(productRepository).save(prod);
    }

    @Test
    void getTotalRevenue_returnsSum() {
        when(purchaseRepository.sumTotalRevenue()).thenReturn(123456.0);
        when(orderRepository.sumTotalRevenue()).thenReturn(1000.0);

        Double sum = purchaseService.getTotalRevenue();
        assertEquals(123456.0 + 1000.0, sum);
    }
}