package com.usuariosminiproyecto.userproyecto.repository;

import com.usuariosminiproyecto.userproyecto.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByDescription(String description);
    // Puedes agregar métodos personalizados de consulta aquí si es necesario
}
