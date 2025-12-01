package com.fitsupplement.backend.controller;

import com.fitsupplement.backend.entity.Purchase;
import com.fitsupplement.backend.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PurchaseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PurchaseService purchaseService;

    @BeforeEach
    void setup() {
        PurchaseController controller = new PurchaseController();
        ReflectionTestUtils.setField(controller, "purchaseService", purchaseService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllPurchases_returnsList() throws Exception {
        Purchase p = new Purchase();
        p.setId(10L);
        Mockito.when(purchaseService.getAllPurchases()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/purchases")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(10));
    }

    @Test
    void getPurchaseById_notFound() throws Exception {
        Mockito.when(purchaseService.getPurchaseById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/purchases/999")).andExpect(status().isNotFound());
    }

    @Test
    void deletePurchase_returnsNoContent() throws Exception {
        Mockito.doNothing().when(purchaseService).deletePurchase(anyLong());
        mockMvc.perform(delete("/api/purchases/5")).andExpect(status().isNoContent());
    }
}
