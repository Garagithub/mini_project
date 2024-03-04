package com.usuariosminiproyecto.userproyecto.service.Startegy;

import com.usuariosminiproyecto.userproyecto.model.User;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotAuthorizedException;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotFoundException;

public class UserStrategy implements UserActionStrategy {
    public void execute(User user) {
        throw new UnsupportedOperationException("No tiene permisos para realizar esta acción.");
    }

    @Override
    public void execute(Long adminUserId, Long userIdToModify, String newRole,boolean activateUser) throws UserNotFoundException, UserNotAuthorizedException {

    }
}

