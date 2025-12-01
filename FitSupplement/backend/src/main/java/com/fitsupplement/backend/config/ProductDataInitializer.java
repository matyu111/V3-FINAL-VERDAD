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
        // Seed products only if table is empty
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
        p.setImagen(getDefaultImageForCategory(categoria));
        return p;
    }

    private String getDefaultImageForCategory(String categoria) {
        switch (String.valueOf(categoria)) {
            case "proteinas":
                return "https://images.unsplash.com/photo-1517341720797-72021962cb87?w=800&q=80&auto=format&fit=crop";
            case "creatina":
                return "https://images.unsplash.com/photo-1519865885898-a43f52a58491?w=800&q=80&auto=format&fit=crop";
            case "pre-entreno":
                return "https://images.unsplash.com/photo-1517960413843-0aee8e2ae918?w=800&q=80&auto=format&fit=crop";
            case "vitaminas":
                return "https://images.unsplash.com/photo-1515396274898-7f3b2f06f37f?w=800&q=80&auto=format&fit=crop";
            default:
                return "https://images.unsplash.com/photo-1514996937319-344454492b37?w=800&q=80&auto=format&fit=crop";
        }
    }
}