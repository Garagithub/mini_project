package com.usuariosminiproyecto.userproyecto.service.Startegy;

import com.usuariosminiproyecto.userproyecto.service.RoleUpdateStrategy;
import org.springframework.stereotype.Component;

@Component
public class AdminStrategyImpl implements RoleUpdateStrategy {


    @Override
    public void execute() {

    }

    @Override
    public String getRol() {
        return "Administrador";
    }
}
