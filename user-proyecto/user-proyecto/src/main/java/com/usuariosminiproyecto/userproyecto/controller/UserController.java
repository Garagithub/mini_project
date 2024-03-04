package com.usuariosminiproyecto.userproyecto.controller;
import com.usuariosminiproyecto.userproyecto.model.Role;
import com.usuariosminiproyecto.userproyecto.model.User;
import com.usuariosminiproyecto.userproyecto.repository.RoleRepository;
import com.usuariosminiproyecto.userproyecto.repository.UserRepository;
import com.usuariosminiproyecto.userproyecto.service.CustomUserResponse;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotAuthorizedException;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotFoundException;
import com.usuariosminiproyecto.userproyecto.service.RoleService;
import com.usuariosminiproyecto.userproyecto.service.Startegy.AdministratorActionStrategy;
import com.usuariosminiproyecto.userproyecto.service.Startegy.ManagerActionStrategy;
import com.usuariosminiproyecto.userproyecto.service.Startegy.UserActionService;
import com.usuariosminiproyecto.userproyecto.service.UserByRoleDTO;
import com.usuariosminiproyecto.userproyecto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository; // Agrega esta línea
    private final AdministratorActionStrategy administratorActionStrategy;
    private final ManagerActionStrategy managerActionStrategy;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository,AdministratorActionStrategy administratorActionStrategy, ManagerActionStrategy managerActionStrategy) { // Modifica este constructor
        this.userService = userService;
        this.userRepository = userRepository; // Agrega esta línea
        this.administratorActionStrategy = administratorActionStrategy;
        this.managerActionStrategy = managerActionStrategy;
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllActiveUsers();
    }



    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getActiveUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.created(URI.create("/api/users/" + savedUser.getId())).body(savedUser);
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Validated @RequestBody User user) {
        if (!userService.getActiveUserById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        user.setId(id);
        User updatedUser = userService.saveUser(user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<CustomUserResponse> getUsersByCriteriaAndPagination(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String rol,
            Pageable pageable) {
        CustomUserResponse response = userService.findUsersByCriteriaAndPagination(nombre, apellido, activo, rol, pageable);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/byRole")
    public List<UserByRoleDTO> getUsersByRole(@RequestParam String role) {
        return userService.getUsersByRoleGrouped(role);
    }
    @PutMapping("/updateRoleAdmin")
    public void updateUserManager(@RequestParam Long adminUserId, @RequestParam Long userIdToModify, @RequestParam String newRole, @RequestParam boolean activateUser) throws UserNotFoundException {
        // Verificar si el usuario que realiza la modificación es un administrador o gerente
        Role adminRole = userService.getRoleByUserId(adminUserId);
        if (adminRole == null || (!adminRole.getDescription().equals("Administrador") && !adminRole.getDescription().equals("Gerente"))) {
            throw new UserNotAuthorizedException("El usuario no tiene permisos de administrador o gerente");
        }

        // Obtener el usuario que se va a modificar
        Optional<User> userOptional = userRepository.findById(userIdToModify);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("No se encontró el usuario con ID: " + userIdToModify);
        }
        User userToUpdate = userOptional.get();

        // Ejecutar la estrategia correspondiente
        if (adminRole.getDescription().equals("Administrador")) {
            administratorActionStrategy.execute(adminUserId, userIdToModify, newRole,activateUser);
        } else if (adminRole.getDescription().equals("Gerente")) {
            managerActionStrategy.execute(adminUserId, userIdToModify, newRole,activateUser);
        }
        else {
            throw new UserNotAuthorizedException("El usuario no tiene permisos de administrador o gerente");
        }
    }



    @GetMapping("/users/{userId}/role")
    public ResponseEntity<Role> getRoleByUserId(@PathVariable Long userId) throws UserNotFoundException {
        Role role = userService.getRoleByUserId(userId);
        return ResponseEntity.ok(role);
    }



}