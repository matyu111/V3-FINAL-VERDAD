package com.fitsupplement.backend.config;

import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Eliminar usuarios demo con dominios inválidos
        userRepository.findByEmail("admin@fitsupplement.local").ifPresent(userRepository::delete);
        userRepository.findByEmail("user@fitsupplement.local").ifPresent(userRepository::delete);

        // Crear/asegurar admin principal con rol ADMIN
        userRepository.findByEmail("admin@duoc.cl").ifPresentOrElse(existing -> {
            if (!"ADMIN".equals(existing.getRole()) || !existing.isActivo()) {
                existing.setRole("ADMIN");
                existing.setActivo(true);
                userRepository.save(existing);
            }
        }, () -> {
            User admin = new User();
            admin.setNombre("Admin");
            admin.setApellido("Duoc");
            admin.setEmail("admin@duoc.cl");
            admin.setTelefono("000000000");
            admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setObjetivos("Administración");
            admin.setAceptoMarketing(false);
            admin.setRole("ADMIN");
            admin.setActivo(true);
            userRepository.save(admin);
        });

        if (!userRepository.existsByEmail("usuario@gmail.com")) {
            User user = new User();
            user.setNombre("Usuario");
            user.setApellido("Gmail");
            user.setEmail("usuario@gmail.com");
            user.setTelefono("000000001");
            user.setFechaNacimiento(LocalDate.of(1995, 1, 1));
            user.setPassword(passwordEncoder.encode("User123!"));
            user.setObjetivos("Compras");
            user.setAceptoMarketing(false);
            user.setRole("USER");
            user.setActivo(true);
            userRepository.save(user);
        }

        if (!userRepository.existsByEmail("profesor@profesor.duoc.cl")) {
            User profesor = new User();
            profesor.setNombre("Profesor");
            profesor.setApellido("Duoc");
            profesor.setEmail("profesor@profesor.duoc.cl");
            profesor.setTelefono("000000002");
            profesor.setFechaNacimiento(LocalDate.of(1985, 1, 1));
            profesor.setPassword(passwordEncoder.encode("Profesor123!"));
            profesor.setObjetivos("Capacitación");
            profesor.setAceptoMarketing(false);
            profesor.setRole("USER");
            profesor.setActivo(true);
            userRepository.save(profesor);
        }
    }
}