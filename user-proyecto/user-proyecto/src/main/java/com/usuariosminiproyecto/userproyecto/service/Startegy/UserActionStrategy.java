package com.usuariosminiproyecto.userproyecto.service.Startegy;

import com.usuariosminiproyecto.userproyecto.model.User;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotAuthorizedException;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotFoundException;

public interface UserActionStrategy {  void execute(Long adminUserId, Long userIdToModify, String newRole, boolean activateUser) throws UserNotFoundException, UserNotAuthorizedException;


}