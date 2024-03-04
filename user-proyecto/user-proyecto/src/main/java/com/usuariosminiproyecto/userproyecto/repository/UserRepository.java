package com.usuariosminiproyecto.userproyecto.repository;
import com.usuariosminiproyecto.userproyecto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByActiveTrue();
    Optional<User> findByIdAndActiveTrue(Long id);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    List<User> findByRoles_Description(String roleName);
    User save(User user);
}