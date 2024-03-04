package com.usuariosminiproyecto.userproyecto;
import com.usuariosminiproyecto.userproyecto.controller.RoleController;
import com.usuariosminiproyecto.userproyecto.controller.UserController;
import com.usuariosminiproyecto.userproyecto.model.Role;
import com.usuariosminiproyecto.userproyecto.model.User;
import com.usuariosminiproyecto.userproyecto.repository.RoleRepository;
import com.usuariosminiproyecto.userproyecto.repository.UserRepository;
import com.usuariosminiproyecto.userproyecto.service.*;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.DuplicateUserException;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotAuthorizedException;
import com.usuariosminiproyecto.userproyecto.service.Expceptions.UserNotFoundException;
import com.usuariosminiproyecto.userproyecto.service.Startegy.AdministratorActionStrategy;
import com.usuariosminiproyecto.userproyecto.service.Startegy.ManagerActionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRepositoryParam userRepositoryParam;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllActiveUsers() {
        List<User> expectedUsers = Arrays.asList(
                new User("Juan", "Perez", true),
                new User("Max", "Verstappen", true)
        );
        when(userRepository.findByActiveTrue()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllActiveUsers();

        assertEquals(expectedUsers.size(), actualUsers.size());
        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i).getFirstName(), actualUsers.get(i).getFirstName());
            assertEquals(expectedUsers.get(i).getLastName(), actualUsers.get(i).getLastName());
            assertEquals(expectedUsers.get(i).isActive(), actualUsers.get(i).isActive());
        }

        verify(userRepository, times(1)).findByActiveTrue();
    }

    @Test
    public void testGetActiveUserById_WhenUserExistsAndIsActive() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setActive(true);
        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getActiveUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertTrue(result.get().isActive());
    }

    @Test
    public void testGetActiveUserById_WhenUserDoesNotExist() {
        Long userId = 1L;
        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getActiveUserById(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSaveUser_WhenDuplicateUserExists() {
        User user = new User();
        user.setFirstName("Miguel");
        user.setLastName("Borja");

        when(userRepository.existsByFirstNameAndLastName(user.getFirstName(), user.getLastName())).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.saveUser(user));
    }

    @Test
    public void testSaveUser_WhenNoDuplicateUserExists() {
        User user = new User();
        user.setFirstName("Miguel");
        user.setLastName("Borja");

        when(userRepository.existsByFirstNameAndLastName(user.getFirstName(), user.getLastName())).thenReturn(false);

        User savedUser = userService.saveUser(user);
    }

    @Test
    public void testGetUsersByRole_WithExistingRole() {
        String role = "admin";
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Juan");
        user1.setLastName("Perez");
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Pedro");
        user2.setLastName("Gomez");
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByRoles_Description(role)).thenReturn(users);

        List<User> result = userService.getUsersByRole(role);

        Assertions.assertEquals(users.size(), result.size());
        Assertions.assertEquals(users.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(users.get(1).getId(), result.get(1).getId());
    }

    @Test
    public void testGetUsersByRole_WithNonExistingRole() {
        String role = "nonexistent_role";
        when(userRepository.findByRoles_Description(role)).thenReturn(Arrays.asList());

        List<User> result = userService.getUsersByRole(role);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetUsersByRoleGrouped_WithExistingRole() {
        String role = "admin";
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Jorge");
        user1.setLastName("Gimenez");
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Joel");
        user2.setLastName("Rodriguez");
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByRoles_Description(role)).thenReturn(users);

        List<UserByRoleDTO> result = userService.getUsersByRoleGrouped(role);

        Assertions.assertEquals(1, result.size());
        UserByRoleDTO userByRoleDTO = result.get(0);
        Assertions.assertEquals(role, userByRoleDTO.getRol());
        Assertions.assertEquals(2, userByRoleDTO.getUsuarios().size());
    }

    @Test
    public void testGetUsersByRoleGrouped_WithNonExistingRole() {
        String role = "nonexistent_role";
        when(userRepository.findByRoles_Description(role)).thenReturn(Arrays.asList());

        List<UserByRoleDTO> result = userService.getUsersByRoleGrouped(role);

        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    public void testUpdateUser_WhenUserNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        User updatedUser = new User();

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(id, updatedUser));
    }

    @Test
    public void testUpdateUser_WhenUserDuplicate() {
        Long id = 1L;
        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setFirstName("Jose");
        existingUser.setLastName("Perez");
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        User updatedUser = new User();
        updatedUser.setFirstName("Josefina");
        updatedUser.setLastName("Perez");

        when(userRepository.existsByFirstNameAndLastName(updatedUser.getFirstName(), updatedUser.getLastName())).thenReturn(true);

        Assertions.assertThrows(DuplicateUserException.class, () -> userService.updateUser(id, updatedUser));
    }

    @Test
    public void testDeleteUser_WhenUserExists() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Daniel");
        existingUser.setLastName("Gomez");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(1L);

        Assertions.assertFalse(existingUser.isActive());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testDeleteUser_WhenUserNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testFindUsersByCriteriaAndPagination() {
        String nombre = "Daniel";
        String apellido = "Gomez";
        Boolean activo = true;
        String rol = "User_rol";
        Pageable pageable = PageRequest.of(0, 10);

        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "Daniel@gmail.com", "Daniel", "Gomez", true, new HashSet<>(Collections.singletonList(new Role(1L, "User_rol")))));

        Page<User> usersPage = new PageImpl<>(userList, pageable, userList.size());

        when(userRepositoryParam.findByCriteriaAndPagination(nombre, apellido, activo, rol, pageable)).thenReturn(usersPage);

        CustomUserResponse result = userService.findUsersByCriteriaAndPagination(nombre, apellido, activo, rol, pageable);

        Assertions.assertEquals(1, result.getUsers().size());
        Assertions.assertEquals(1, result.getTotalPages());
        Assertions.assertEquals(1, result.getTotalUsers());
        Assertions.assertEquals(10, result.getPageSize());
        Assertions.assertEquals("Daniel", result.getUsers().get(0).getNombre());
        Assertions.assertEquals("Gomez", result.getUsers().get(0).getApellido());
        Assertions.assertEquals(1, result.getUsers().get(0).getActivo());
        Assertions.assertEquals(Collections.singletonList("User_rol"), result.getUsers().get(0).getRoles().stream().map(Role::getDescription).collect(Collectors.toList()));

    }

    @Test
    public void testGetRoleByUserId_WhenUserExistsAndHasRole() throws UserNotFoundException {
        Long userId = 1L;
        Role expectedRole = new Role(1L, "User_rol");

        User user = new User();
        user.setId(userId);
        user.setRoles(Collections.singleton(expectedRole));

        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.of(user));

        Role result = userService.getRoleByUserId(userId);

        assertEquals(expectedRole, result);
        verify(userRepository, times(1)).findByIdAndActiveTrue(userId);
    }

    @Test
    public void testGetRoleByUserId_WhenUserExistsAndHasNoRole() throws UserNotFoundException {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setRoles(Collections.emptySet());

        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.of(user));

        assertThrows(UserNotFoundException.class, () -> userService.getRoleByUserId(userId));
        verify(userRepository, times(1)).findByIdAndActiveTrue(userId);
    }

    @Test
    public void testGetRoleByUserId_WhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getRoleByUserId(userId));
        verify(userRepository, times(1)).findByIdAndActiveTrue(userId);
    }

    @Test
    public void testAsignarRol_WhenUserExistsAndRoleExists() throws UserNotFoundException {
        Long userId = 1L;
        String rolDescripcion = "User_rol";

        User user = new User();
        user.setId(userId);

        Role role = new Role();
        role.setDescription(rolDescripcion);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByDescription(rolDescripcion)).thenReturn(Optional.of(role));

        userService.asignarRol(userId, rolDescripcion);

        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findByDescription(rolDescripcion);
        verify(userRepository, times(1)).save(user);
    }
}
