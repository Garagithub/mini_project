package com.usuariosminiproyecto.userproyecto.dto;

import java.util.List;

public class UserByRoleDTO {
    private String rol;

    public UserByRoleDTO(String rol, List<UserInfoDTO> usuarios) {
        this.rol = rol;
        this.usuarios = usuarios;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<UserInfoDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UserInfoDTO> usuarios) {
        this.usuarios = usuarios;
    }

    private List<UserInfoDTO> usuarios;

    // Constructor, getters y setters
}
