package com.fitsupplement.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    // Dirección del usuario (opcional)
    private String direccion;

    @Column(nullable = false)
    private String password; // Debe ser hasheada en el servicio

    @Column(nullable = false)
    private String objetivos;

    private boolean aceptoMarketing;

    @Column(nullable = false)
    private String role = "USER"; // 'ADMIN' or 'USER'

    @Column(nullable = false)
    private boolean activo = true;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getPassword() {
        return password;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public boolean isAceptoMarketing() {
        return aceptoMarketing;
    }

    public String getRole() {
        return role;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public void setAceptoMarketing(boolean aceptoMarketing) {
        this.aceptoMarketing = aceptoMarketing;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}