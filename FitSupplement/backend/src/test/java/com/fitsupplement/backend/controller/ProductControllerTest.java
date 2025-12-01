package com.fitsupplement.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitsupplement.backend.entity.Product;
import com.fitsupplement.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        ProductController controller = new ProductController();
        // inject mock service into controller
        ReflectionTestUtils.setField(controller, "productService", productService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllProducts_returnsList() throws Exception {
        Product p = new Product();
        p.setId(1L);
        p.setNombre("Proteina X");
        p.setPrecio(24900.0);
        p.setStock(10);
        p.setCategoria("proteinas");

        Mockito.when(productService.getAllProducts()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Proteina X"));
    }

    @Test
    void getProductById_notFound() throws Exception {
        Mockito.when(productService.getProductById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_returnsCreated() throws Exception {
        Product toCreate = new Product();
        toCreate.setNombre("Nuevo");
        toCreate.setPrecio(15000.0);
        toCreate.setStock(5);
        toCreate.setCategoria("vitaminas");

        Product created = new Product();
        created.setId(55L);
        created.setNombre(toCreate.getNombre());
        created.setPrecio(toCreate.getPrecio());
        created.setStock(toCreate.getStock());
        created.setCategoria(toCreate.getCategoria());

        Mockito.when(productService.createProduct(any(Product.class))).thenReturn(created);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55));
    }

}
