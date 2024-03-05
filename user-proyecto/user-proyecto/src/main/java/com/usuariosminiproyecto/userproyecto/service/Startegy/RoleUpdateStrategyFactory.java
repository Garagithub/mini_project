package com.usuariosminiproyecto.userproyecto.service.Startegy;

import com.usuariosminiproyecto.userproyecto.service.RoleUpdateStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoleUpdateStrategyFactory {

    private final Map<String, RoleUpdateStrategy> strategiesMap = new HashMap<>();

    public RoleUpdateStrategyFactory(List<RoleUpdateStrategy> strategies) {
        strategies.forEach(strategy -> strategiesMap.put(strategy.getRol(), strategy));
    }

    public void execute(String descripcion) {
        RoleUpdateStrategy roleUpdateStrategy = strategiesMap.get(descripcion);

        if (roleUpdateStrategy == null)
            throw new IllegalStateException("Not implemented");

        roleUpdateStrategy.execute();
    }

}
