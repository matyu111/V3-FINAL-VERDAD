package com.fitsupplement.backend.config;

import com.fitsupplement.backend.entity.Product;
import com.fitsupplement.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        // Sembrar productos solo si no existen
        if (productRepository.count() == 0) {
            List<Product> initialProducts = Arrays.asList(
                buildProduct("Proteína Whey Premium", "Proteína de suero de leche de alta calidad para recuperación muscular óptima.", 29990.0, 50, "proteinas"),
                buildProduct("Proteína Caseína", "Absorción lenta ideal para tomar antes de dormir.", 25990.0, 40, "proteinas"),
                buildProduct("Creatina Monohidratada", "Aumenta tu fuerza y potencia muscular con creatina pura.", 19990.0, 30, "creatina"),
                buildProduct("Creatina HCL", "Forma más soluble de creatina para mejor absorción.", 22990.0, 25, "creatina"),
                buildProduct("Pre-entreno Explosivo", "Energía instantánea y enfoque mental para entrenamientos intensos.", 24990.0, 35, "pre-entreno"),
                buildProduct("Pre-entreno Natural", "Energía natural sin estimulantes artificiales.", 19990.0, 20, "pre-entreno"),
                buildProduct("Multivitamínico Completo", "Todas las vitaminas y minerales esenciales en una cápsula.", 15990.0, 60, "vitaminas"),
                buildProduct("Vitamina D3 + K2", "Combinación perfecta para huesos y sistema inmunológico.", 12990.0, 45, "vitaminas")
            );

            productRepository.saveAll(initialProducts);
        }
    }

    private Product buildProduct(String nombre, String descripcion, Double precio, Integer stock, String categoria) {
        Product p = new Product();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setCategoria(categoria);
        p.setActivo(true);
        return p;
    }
}