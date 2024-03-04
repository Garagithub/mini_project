package com.usuariosminiproyecto.userproyecto.model;
import com.usuariosminiproyecto.userproyecto.repository.RoleRepository;
import com.usuariosminiproyecto.userproyecto.repository.UserRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.usuariosminiproyecto.userproyecto.model.Role;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "El login debe ser una dirección de correo electrónico válida")
    @NotBlank(message = "El login es obligatorio")
    @Column(nullable = false, unique = true)
    private String login;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User() {
        this.roles = new HashSet<>();

    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public User(Long id, String login, String firstName, String lastName, boolean active, Set<Role> roles) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.roles = roles;
    }
    public User(String firstName, String lastName, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }
    public User(long l, String jane, String smith, boolean b, Object o) {
    }





    public static class UserQueryParams {
        private String nombre;
        private String apellido;
        private Boolean activo;
        private String rol;
        private int pageSize;

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

        public Boolean getActivo() {
            return activo;
        }

        public void setActivo(Boolean activo) {
            this.activo = activo;
        }

        public String getRol() {
            return rol;
        }

        public void setRol(String rol) {
            this.rol = rol;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        private int pageNumber;

        // Getters y setters
    }


}