package com.usuariosminiproyecto.userproyecto.service;

import com.usuariosminiproyecto.userproyecto.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

// UserRepository.java
@Repository
public class UserRepositoryParam {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<User> findByCriteriaAndPagination(String nombre, String apellido, Boolean activo, String rol, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        // Lista de predicados para aplicar filtros
        List<Predicate> predicates = new ArrayList<>();

        // Aplicar filtros opcionales
        if (nombre != null) {
            predicates.add(cb.equal(root.get("firstName"), nombre));
        }
        if (apellido != null) {
            predicates.add(cb.equal(root.get("lastName"), apellido));
        }
        if (activo != null) {
            predicates.add(cb.equal(root.get("active"), activo));
        }
        if (rol != null) {
            predicates.add(cb.equal(root.join("roles").get("description"), rol));
        }

        // Construir y aplicar los predicados
        cq.where(predicates.toArray(new Predicate[0]));

        // Contar el total de resultados
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(User.class)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        // Ejecutar la consulta paginada
        TypedQuery<User> typedQuery = entityManager.createQuery(cq);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<User> users = typedQuery.getResultList();

        // Crear un objeto Page con los resultados y la información de paginación
        return new PageImpl<>(users, pageable, count);
    }

    public Page<User> findActiveUsersByCriteriaAndPagination(String nombre, String apellido, String rol, Pageable pageable) {
        @Repository
        class UserRepositoryActiveUser {

            @PersistenceContext
            private EntityManager entityManager;

            public Page<User> findActiveUsersByCriteriaAndPagination(String nombre, String apellido, String rol, Pageable pageable) {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<User> cq = cb.createQuery(User.class);
                Root<User> root = cq.from(User.class);

                // Lista de predicados para aplicar filtros
                List<Predicate> predicates = new ArrayList<>();

                // Filtro para usuarios activos
                predicates.add(cb.isTrue(root.get("activo")));

                // Aplicar filtros opcionales
                if (nombre != null && !nombre.isEmpty()) {
                    predicates.add(cb.equal(root.get("firstName"), nombre));
                }
                if (apellido != null && !apellido.isEmpty()) {
                    predicates.add(cb.equal(root.get("lastName"), apellido));
                }
                if (rol != null && !rol.isEmpty()) {
                    predicates.add(cb.equal(root.join("roles").get("descripcion"), rol));
                }

                // Construir y aplicar los predicados
                cq.where(predicates.toArray(new Predicate[0]));

                // Contar el total de resultados
                CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
                countQuery.select(cb.count(countQuery.from(User.class)));
                Long count = entityManager.createQuery(countQuery).getSingleResult();

                // Ejecutar la consulta paginada
                TypedQuery<User> typedQuery = entityManager.createQuery(cq);
                typedQuery.setFirstResult((int) pageable.getOffset());
                typedQuery.setMaxResults(pageable.getPageSize());
                List<User> users = typedQuery.getResultList();

                // Crear un objeto Page con los resultados y la información de paginación
                return new PageImpl<>(users, pageable, count);
            }
        }

        return null;
    }
}
