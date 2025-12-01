package com.fitsupplement.backend.controller;

import com.fitsupplement.backend.service.PurchaseService;
import com.fitsupplement.backend.service.ProductService;
import com.fitsupplement.backend.service.UserService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    // Stubs sencillos que devuelven valores constantes sin depender de beans
    static class StubUserService extends UserService {
        @Override
        public long getTotalUsers() { return 7L; }
    }

    static class StubProductService extends ProductService {
        @Override
        public long getTotalProducts() { return 25L; }
    }

    static class StubPurchaseService extends PurchaseService {
        @Override
        public long getTotalPurchases() { return 15L; }
        @Override
        public Double getTotalRevenue() { return 99999.0; }
    }

    @Test
    void getDashboardStats_returnsExpectedValues() throws Exception {
        AdminController adminController = new AdminController();

        // Inyección por reflexión
        Field fUser = AdminController.class.getDeclaredField("userService");
        fUser.setAccessible(true);
        fUser.set(adminController, new StubUserService());

        Field fProd = AdminController.class.getDeclaredField("productService");
        fProd.setAccessible(true);
        fProd.set(adminController, new StubProductService());

        Field fPur = AdminController.class.getDeclaredField("purchaseService");
        fPur.setAccessible(true);
        fPur.set(adminController, new StubPurchaseService());

        Map<String, Object> stats = adminController.getDashboardStats();
        assertEquals(7L, stats.get("totalUsers"));
        assertEquals(25L, stats.get("totalProducts"));
        assertEquals(15L, stats.get("totalPurchases"));
        assertEquals(99999.0, stats.get("totalRevenue"));
    }
}