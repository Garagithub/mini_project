package com.usuariosminiproyecto.userproyecto.service.Startegy;

import com.usuariosminiproyecto.userproyecto.model.User;
import org.apache.coyote.Request;

public class UserActionService {
    private static UserActionStrategy strategy;

    public static void setStrategy(UserActionStrategy strategy) {
        UserActionService.strategy = strategy;
    }

    /*public static void performAction(User request) {
        strategy.execute(request);
    }*/
}
