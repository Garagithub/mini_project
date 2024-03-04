package com.usuariosminiproyecto.userproyecto.service;

import com.usuariosminiproyecto.userproyecto.model.Role;
import com.usuariosminiproyecto.userproyecto.model.User;
import com.usuariosminiproyecto.userproyecto.repository.RoleRepository;
import com.usuariosminiproyecto.userproyecto.repository.UserRepository;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.DuplicateUserException;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRepositoryParam userRepositoryParam;


    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,UserRepositoryParam userRepositoryParam) {
        this.userRepository = userRepository;
        this.roleRepository=roleRepository;
        this.userRepositoryParam=userRepositoryParam;
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    public Optional<User> getActiveUserById(Long id) {
        return userRepository.findByIdAndActiveTrue(id);
    }

    public User saveUser(User user) {
        if (userRepository.existsByFirstNameAndLastName(user.getFirstName(), user.getLastName())) {
            throw new DuplicateUserException("A user with the same first name and last name already exists.");
        }
        return userRepository.save(user);
    }
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRoles_Description(role);
    }
    public List<UserByRoleDTO> getUsersByRoleGrouped(String role) {
        List<User> users = getUsersByRole(role);

        // Agrupar usuarios por rol
        Map<String, List<UserInfoDTO>> usersByRole = new HashMap<>();
        for (User user : users) {
            usersByRole.computeIfAbsent(role, k -> new ArrayList<>())
                    .add(new UserInfoDTO(user.getFirstName(), user.getLastName()));
        }

        // Crear lista de UserByRoleDTO
        List<UserByRoleDTO> result = new ArrayList<>();
        usersByRole.forEach((rol, usuarios) -> result.add(new UserByRoleDTO(rol, usuarios)));

        return result;
    }



    public User updateUser(Long id, User user) throws UserNotFoundException {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        User existingUser = existingUserOptional.get();

        if (!existingUser.getFirstName().equals(user.getFirstName()) || !existingUser.getLastName().equals(user.getLastName())) {
            if (userRepository.existsByFirstNameAndLastName(user.getFirstName(), user.getLastName())) {
                throw new DuplicateUserException("A user with the same first name and last name already exists.");
            }
        }

        user.setId(id);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        user.setActive(false); // Cambiar el estado a inactivo
        userRepository.save(user); // Guardar los cambios en la base de datos
    }

    @PersistenceContext
    private EntityManager entityManager;


    public CustomUserResponse findUsersByCriteriaAndPagination(String nombre, String apellido, Boolean activo, String rol, Pageable pageable) {
        Page<User> usersPage = userRepositoryParam.findByCriteriaAndPagination(nombre, apellido, activo, rol, pageable);

        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : usersPage.getContent()) {
            UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.isActive() ? 1 : 0, user.getRoles());
            userDTOList.add(userDTO);
        }

        return new CustomUserResponse(userDTOList, usersPage.getTotalPages(), usersPage.getTotalElements(), usersPage.getSize());
    }


    public Role getRoleByUserId(Long userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByIdAndActiveTrue(userId);
        return userOptional.map(user -> {
            Set<Role> roles = user.getRoles().stream() // Llama a stream() en el Optional
                    .collect(Collectors.toSet());
            if (!roles.isEmpty()) {
                return roles.iterator().next();
            } else {
                return null;
            }
        }).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }



    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }


    public void asignarRol(Long userId, String rolDescripcion) throws UserNotFoundException {
        // Obtener el usuario por su ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        User user = userOptional.get();

        // Obtener el rol por su descripción
        Optional<Role> roleOptional = roleRepository.findByDescription(rolDescripcion);
        if (roleOptional.isEmpty()) {
            throw new IllegalArgumentException("El rol especificado no existe: " + rolDescripcion);
        }
        Role rol = roleOptional.get();

        // Limpiar roles anteriores y asignar el nuevo rol al usuario
        user.getRoles().clear();
        user.getRoles().add(rol);

        // Guardar los cambios en la base de datos
        userRepository.save(user);
    }


}
