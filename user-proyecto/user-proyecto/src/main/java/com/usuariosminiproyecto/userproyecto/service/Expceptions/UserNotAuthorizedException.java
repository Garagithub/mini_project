package com.usuariosminiproyecto.userproyecto.service.Expceptions;
public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException(String message) {
        super(message);
    }
}