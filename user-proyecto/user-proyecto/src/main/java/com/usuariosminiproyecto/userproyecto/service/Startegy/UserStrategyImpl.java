package com.usuariosminiproyecto.userproyecto.service.Startegy;

import com.usuariosminiproyecto.userproyecto.service.RoleUpdateStrategy;
import org.springframework.stereotype.Component;

@Component
public class UserStrategyImpl implements RoleUpdateStrategy {

    @Override
    public void execute() {
        throw new RuntimeException();
    }

    @Override
    public String getRol() {
        return "Usuario";
    }
}
