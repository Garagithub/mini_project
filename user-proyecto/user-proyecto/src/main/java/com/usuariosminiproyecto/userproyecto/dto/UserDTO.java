package com.usuariosminiproyecto.userproyecto.dto;
import com.usuariosminiproyecto.userproyecto.model.Role;

import java.util.List;
import java.util.Set;

public class UserDTO {
    private String nombre;
    private String apellido;
    private int activo;
    private Set<Role> roles;

    public UserDTO(String nombre, String apellido, int activo, Set<Role> roles) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = activo;
        this.roles = roles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}