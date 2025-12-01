package com.fitsupplement.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.repository.UserRepository;
import com.fitsupplement.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, UserService userService, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@profesor.com").isEmpty()) {
                User admin = new User();
                admin.setNombre("Admin");
                admin.setApellido("Profesor");
                admin.setEmail("admin@profesor.com");
                admin.setTelefono("123456789");
                admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
                // Guardar directamente para preservar rol ADMIN
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                admin.setObjetivos("Administración");
                admin.setAceptoMarketing(false);
                admin.setRole("ADMIN");
                admin.setActivo(true);
                userRepository.save(admin);
                System.out.println("Usuario admin (admin@profesor.com) creado con rol ADMIN.");
            }
        };
    }
}