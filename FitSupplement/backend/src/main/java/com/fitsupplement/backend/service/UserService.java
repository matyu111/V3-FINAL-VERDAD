package com.fitsupplement.backend.service;

import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fitsupplement.backend.repository.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        Objects.requireNonNull(user, "user no puede ser null");
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email ya existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Solo establecer rol por defecto si no viene definido
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }
        // Dirección es opcional; solo se persiste si viene informada
        if (user.getDireccion() != null && user.getDireccion().isBlank()) {
            user.setDireccion(null);
        }
        return userRepository.save(user);
    }

    public User updateUser(long id, User userDetails) {
        Objects.requireNonNull(userDetails, "userDetails no puede ser null");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setNombre(userDetails.getNombre());
        user.setApellido(userDetails.getApellido());
        user.setEmail(userDetails.getEmail());
        user.setTelefono(userDetails.getTelefono());
        user.setFechaNacimiento(userDetails.getFechaNacimiento());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setObjetivos(userDetails.getObjetivos());
        user.setAceptoMarketing(userDetails.isAceptoMarketing());
        // Actualizar dirección si se proporciona (permitir vacía para limpiar)
        user.setDireccion((userDetails.getDireccion() != null && userDetails.getDireccion().isBlank()) ? null : userDetails.getDireccion());
        
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        // Bloquear eliminación del usuario placeholder del sistema
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if ("deleted@system.local".equalsIgnoreCase(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el usuario placeholder del sistema.");
        }

        // Reasignar compras al usuario placeholder en vez de borrarlas
        User placeholder = getOrCreateDeletedUserPlaceholder();

        List<com.fitsupplement.backend.entity.Purchase> purchases = purchaseRepository.findByUserId(id);
        for (com.fitsupplement.backend.entity.Purchase p : purchases) {
            p.setUser(placeholder);
            purchaseRepository.save(p);
        }

        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User toggleActive(long id) {
        User user = getUserById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if ("deleted@system.local".equalsIgnoreCase(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede cambiar el estado del usuario placeholder del sistema.");
        }
        user.setActivo(!user.isActivo());
        return userRepository.save(user);
    }

    public long getTotalUsers() {
        // Excluir del conteo al usuario placeholder del sistema
        return userRepository.countByEmailNot("deleted@system.local");
    }

    private User getOrCreateDeletedUserPlaceholder() {
        return userRepository.findByEmail("deleted@system.local")
                .map(u -> {
                    // Asegurar que el placeholder siempre esté inactivo
                    if (Boolean.TRUE.equals(u.isActivo())) {
                        u.setActivo(false);
                        userRepository.save(u);
                    }
                    return u;
                })
                .orElseGet(() -> {
                    User u = new User();
                    u.setNombre("Usuario");
                    u.setApellido("Eliminado");
                    u.setEmail("deleted@system.local");
                    u.setTelefono("000000000");
                    u.setFechaNacimiento(java.time.LocalDate.of(1970, 1, 1));
                    u.setPassword(passwordEncoder.encode("DELETED_PLACEHOLDER"));
                    u.setObjetivos("N/A");
                    u.setAceptoMarketing(false);
                    u.setRole("USER");
                    u.setActivo(false);
                    return userRepository.save(u);
                });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                .build();
    }
}