package com.fitsupplement.backend.controller;

import com.fitsupplement.backend.entity.User;
import com.fitsupplement.backend.util.JwtUtil;
import com.fitsupplement.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        AuthenticationController controller = new AuthenticationController();
        ReflectionTestUtils.setField(controller, "userService", userService);
        ReflectionTestUtils.setField(controller, "authenticationManager", authenticationManager);
        ReflectionTestUtils.setField(controller, "userDetailsService", userDetailsService);
        ReflectionTestUtils.setField(controller, "jwtUtil", jwtUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void login_success_returnsToken() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword("$2a$10$hashed");
        user.setNombre("Test");
        user.setApellido("User");
        user.setRole("USER");
        user.setActivo(true);
        Mockito.when(userService.findByEmail(eq("user@example.com"))).thenReturn(Optional.of(user));
        Mockito.when(jwtUtil.generateToken(any())).thenReturn("mock-jwt-token");

        var payload = new java.util.HashMap<String, String>();
        payload.put("email", "user@example.com");
        payload.put("password", "password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("mock-jwt-token"));
    }

    @Test
    void login_badCredentials_returns401() throws Exception {
        // Ensure user exists so controller proceeds to
        // authenticationManager.authenticate()
        User user = new User();
        user.setId(2L);
        user.setEmail("noone@example.com");
        user.setPassword("$2a$10$hashed");
        user.setNombre("NoOne");
        user.setApellido("Test");
        user.setRole("USER");
        user.setActivo(true);
        Mockito.when(userService.findByEmail(eq("noone@example.com"))).thenReturn(Optional.of(user));

        Mockito.doThrow(new BadCredentialsException("Bad creds")).when(authenticationManager).authenticate(any());

        var payload = new java.util.HashMap<String, String>();
        payload.put("email", "noone@example.com");
        payload.put("password", "wrong");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
