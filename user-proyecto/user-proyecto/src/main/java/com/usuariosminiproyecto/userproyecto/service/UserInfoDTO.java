package com.usuariosminiproyecto.userproyecto.service;
public class UserInfoDTO {
    public UserInfoDTO(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
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

    private String nombre;
    private String apellido;

    // Constructor, getters y setters
}
