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
public class AdministratorActionStrategy implements UserActionStrategy {
    private static UserService userService;
    private static UserRepository userRepository;
    public AdministratorActionStrategy(UserService userService, UserRepository userRepository) { // Modifica este constructor
        this.userService = userService;
        this.userRepository = userRepository; // Agrega esta línea
    }


    @Override
    public void execute(Long adminUserId, Long userIdToModify, String newRole,boolean activateUser) throws UserNotFoundException, UserNotAuthorizedException {
        // Verificar si el usuario que realiza la modificación es un administrador
        Role adminRole = userService.getRoleByUserId(adminUserId);
        if (adminRole == null || !adminRole.getDescription().equals("Administrador")) {
            throw new UserNotAuthorizedException("El usuario no tiene permisos de administrador");
        }

        // Obtener el usuario que se va a modificar
        Optional<User> userOptional = userRepository.findById(userIdToModify);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("No se encontró el usuario con ID: " + userIdToModify);
        }
        User userToUpdate = userOptional.get();

        // Cambiar el rol del usuario
        userService.asignarRol(userIdToModify, newRole);

        // Activar o desactivar al usuario según el parámetro proporcionado
        userToUpdate.setActive(activateUser);

        // Guardar el usuario actualizado en la base de datos
        userRepository.save(userToUpdate);
    }


}