package com.usuariosminiproyecto.userproyecto.service.Startegy;

import com.usuariosminiproyecto.userproyecto.model.Role;
import com.usuariosminiproyecto.userproyecto.model.User;
import com.usuariosminiproyecto.userproyecto.repository.UserRepository;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotAuthorizedException;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotFoundException;
import com.usuariosminiproyecto.userproyecto.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class ManagerActionStrategy implements UserActionStrategy {
    private final UserService userService;
    private final UserRepository userRepository;

    public ManagerActionStrategy(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(Long adminUserId, Long userIdToModify, String newRole,boolean activateUser) throws UserNotFoundException, UserNotAuthorizedException {
        // Verificar si el usuario que realiza la modificación es un gerente
        Role managerRole = userService.getRoleByUserId(adminUserId);
        if (managerRole == null || !managerRole.getDescription().equals("Gerente")) {
            throw new UserNotAuthorizedException("El usuario no tiene permisos de gerente");
        }

        // Obtener el usuario que se va a modificar
        Optional<User> userOptional = userRepository.findById(userIdToModify);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("No se encontró el usuario con ID: " + userIdToModify);
        }
        User userToUpdate = userOptional.get();

        // Cambiar el estado "active" del usuario
        userToUpdate.setActive(!userToUpdate.isActive());

        // Guardar el usuario actualizado en la base de datos
        userRepository.save(userToUpdate);
    }
}