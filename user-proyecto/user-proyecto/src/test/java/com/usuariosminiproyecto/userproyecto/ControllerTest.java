package com.usuariosminiproyecto.userproyecto;

import com.usuariosminiproyecto.userproyecto.controller.RoleController;
import com.usuariosminiproyecto.userproyecto.controller.UserController;
import com.usuariosminiproyecto.userproyecto.model.Role;
import com.usuariosminiproyecto.userproyecto.model.User;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AdministratorActionStrategy administratorActionStrategy;

    @Mock
    private ManagerActionStrategy managerActionStrategy;

    @InjectMocks
    private UserController userController;
    @InjectMocks
    private RoleController roleController;
    @Mock
    private RoleService roleService;

    @Test
    public void testGetUserById() throws UserNotFoundException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userService.getActiveUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setId(1L);
        when(userService.saveUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        assertEquals("/api/users/1", response.getHeaders().getLocation().toString());
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userService.getActiveUserById(userId)).thenReturn(Optional.of(user));
        when(userService.saveUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(userId, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testDeleteUser() throws UserNotFoundException {
        Long userId = 1L;

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    public void testGetUsersByCriteriaAndPagination() {
        String nombre = "Juan";
        String apellido = "Garassino";
        Boolean activo = true;
        String rol = "Administrador";
        CustomUserResponse customUserResponse = new CustomUserResponse(Collections.emptyList(), 0, 0, 0);

        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        AdministratorActionStrategy administratorActionStrategyMock = Mockito.mock(AdministratorActionStrategy.class);
        ManagerActionStrategy managerActionStrategyMock = Mockito.mock(ManagerActionStrategy.class);

        UserController userController = new UserController(userService, userRepositoryMock, administratorActionStrategyMock, managerActionStrategyMock);

        when(userService.findUsersByCriteriaAndPagination(nombre, apellido, activo, rol, null)).thenReturn(customUserResponse);

        ResponseEntity<CustomUserResponse> response = userController.getUsersByCriteriaAndPagination(nombre, apellido, activo, rol, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customUserResponse, response.getBody());
    }

    @Test
    public void testGetRoleById() {
        Long roleId = 1L;
        Role mockRole = new Role(roleId, "Administrador");
        when(roleService.getRoleById(roleId)).thenReturn(Optional.of(mockRole));

        ResponseEntity<Role> response = roleController.getRoleById(roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRole, response.getBody());
    }

    @Test
    public void testGetRoleById_NotFound() {
        Long roleId = 1L;
        when(roleService.getRoleById(roleId)).thenReturn(Optional.empty());

        ResponseEntity<Role> response = roleController.getRoleById(roleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateRole() {
        Role roleToCreate = new Role();
        Role savedRole = new Role(1L, "Administrador");
        when(roleService.saveRole(any(Role.class))).thenReturn(savedRole);

        ResponseEntity<Role> response = roleController.createRole(roleToCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedRole, response.getBody());
    }

    @Test
    public void testUpdateRole() {
        Long roleId = 1L;
        Role updatedRole = new Role(roleId, "Updated_rol");
        when(roleService.getRoleById(roleId)).thenReturn(Optional.of(new Role()));
        when(roleService.saveRole(any(Role.class))).thenReturn(updatedRole);

        ResponseEntity<Role> response = roleController.updateRole(roleId, updatedRole);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRole, response.getBody());
    }

    @Test
    public void testDeleteRole() {
        Long roleId = 1L;
        doNothing().when(roleService).deleteRole(roleId);

        ResponseEntity<Void> response = roleController.deleteRole(roleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roleService).deleteRole(roleId);
    }
}
