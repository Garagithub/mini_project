package com.usuariosminiproyecto.userproyecto;

import com.usuariosminiproyecto.userproyecto.controller.RoleController;
import com.usuariosminiproyecto.userproyecto.controller.UserController;
import com.usuariosminiproyecto.userproyecto.model.Role;
import com.usuariosminiproyecto.userproyecto.model.User;
import com.usuariosminiproyecto.userproyecto.repository.RoleRepository;
import com.usuariosminiproyecto.userproyecto.repository.UserRepository;
import com.usuariosminiproyecto.userproyecto.service.*;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotAuthorizedException;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotFoundException;
import com.usuariosminiproyecto.userproyecto.service.Startegy.AdministratorActionStrategy;
import com.usuariosminiproyecto.userproyecto.service.Startegy.ManagerActionStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RepositoryTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    @Test
    public void testFindByDescription() {
        String description = "User_rol";
        Role expectedRole = new Role(1L, description);

        when(roleRepository.findByDescription(description)).thenReturn(Optional.of(expectedRole));

        Optional<Role> actualRoleOptional = roleRepository.findByDescription(description);

        assertEquals(expectedRole, actualRoleOptional.orElse(null));
    }

    @Test
    public void testFindByActiveTrue() {
        User user1 = new User(1L, "Juan", "Dominguez", true, null);
        User user2 = new User(2L, "Jose", "Sanchez", true, null);
        User user3 = new User(3L, "Alicia", "Garcia", false, null);

        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.findByActiveTrue()).thenReturn(expectedUsers);

        List<User> actualUsers = userRepository.findByActiveTrue();

        assertEquals(expectedUsers.size(), actualUsers.size());
        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i), actualUsers.get(i));
        }
    }

    @Test
    public void testFindByIdAndActiveTrue_WhenUserExists() {
        Long userId = 1L;
        User user = new User(userId, "test@example.com", "Juan", "Dominguez", true, null);
        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByIdAndActiveTrue(userId);

        assertEquals(user, foundUser.orElse(null));
    }

    @Test
    public void testFindByIdAndActiveTrue_WhenUserDoesNotExist() {
        Long userId = 1L;
        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findByIdAndActiveTrue(userId);

        assertEquals(Optional.empty(), foundUser);
    }

    @Test
    public void testExistsByFirstNameAndLastName_WhenUserExists() {
        String firstName = "Juan";
        String lastName = "Perez";
        when(userRepository.existsByFirstNameAndLastName(firstName, lastName)).thenReturn(true);

        boolean exists = userRepository.existsByFirstNameAndLastName(firstName, lastName);

        assertEquals(true, exists);
    }

    @Test
    public void testExistsByFirstNameAndLastName_WhenUserDoesNotExist() {
        String firstName = "Flor";
        String lastName = "Perez";
        when(userRepository.existsByFirstNameAndLastName(firstName, lastName)).thenReturn(false);

        boolean exists = userRepository.existsByFirstNameAndLastName(firstName, lastName);

        assertEquals(false, exists);
    }

    @Test
    public void testFindByRoles_Description_WhenRoleExists() {
        String roleName = "User_rol";
        Role role = new Role(1L, roleName);
        User user1 = new User(1L, "juangara99@example.com", "Juan", "Garassino", true, Set.of(role));
        User user2 = new User(2L, "Napoleon@example.com", "Marcelo", "Gallardo", true, Set.of(role));
        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.findByRoles_Description(roleName)).thenReturn(expectedUsers);

        List<User> foundUsers = userRepository.findByRoles_Description(roleName);

        assertEquals(expectedUsers.size(), foundUsers.size());
        assertEquals(expectedUsers, foundUsers);
    }

    @Test
    public void testFindByRoles_Description_WhenRoleDoesNotExist() {
        String roleName = "Administrador_rol";
        when(userRepository.findByRoles_Description(roleName)).thenReturn(List.of());

        List<User> foundUsers = userRepository.findByRoles_Description(roleName);

        assertEquals(0, foundUsers.size());
    }
}
