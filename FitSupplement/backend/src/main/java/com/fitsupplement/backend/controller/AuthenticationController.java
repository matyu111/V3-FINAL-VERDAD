package com.fitsupplement.backend.controller;

import com.fitsupplement.backend.util.JwtUtil;
import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Optional<User> userOpt = userService.findByEmail(authenticationRequest.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El correo no existe"));
        }

        User user = userOpt.get();
        if (!user.isActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "La cuenta está inactiva"));
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of(
                "jwt", jwt,
                "user", Map.of(
                        "id", user.getId(),
                        "nombre", user.getNombre(),
                        "apellido", user.getApellido(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "activo", user.isActivo()
                )
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User created = userService.createUser(user);
            // Auto-login after successful registration: issue JWT
            final UserDetails userDetails = userDetailsService.loadUserByUsername(created.getEmail());
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "jwt", jwt,
                "user", Map.of(
                    "id", created.getId(),
                    "email", created.getEmail(),
                    "nombre", created.getNombre(),
                    "apellido", created.getApellido(),
                    "role", created.getRole(),
                    "activo", created.isActivo()
                )
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token faltante"));
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
        }
        User user = userOpt.get();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "nombre", user.getNombre(),
                "apellido", user.getApellido(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "activo", user.isActivo()
        ));
    }
}

class AuthenticationRequest {
    private String email;
    private String password;

    // getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}