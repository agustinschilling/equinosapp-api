package com.example.equinosappapi.controllers;

import com.example.equinosappapi.models.User;
import com.example.equinosappapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        User user = new User();
        //user.setId(1L);
        when(userService.readOne(anyLong())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserById_shouldReturnNotFound_whenUserDoesNotExist() {
        when(userService.readOne(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getUserByUsername_shouldReturnUser_whenUserExists() {
        User user = new User();
        user.setUsername("testuser");
        when(userService.getByUsername(anyString())).thenReturn(user);

        ResponseEntity<User> response = userController.getUserByUsername("testuser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserByUsername_shouldReturnNotFound_whenUserDoesNotExist() {
        when(userService.getByUsername(anyString())).thenReturn(null);

        ResponseEntity<User> response = userController.getUserByUsername("testuser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser_whenUserExists() {
        User existingUser = new User();
        //existingUser.setId(1L);
        existingUser.setUsername("existinguser");

        User updatedDetails = new User();
        updatedDetails.setUsername("updateduser");

        when(userService.readOne(anyLong())).thenReturn(Optional.of(existingUser));
        //when(userService.update(any(User.class))).thenReturn(existingUser);

        ResponseEntity<User> response = userController.updateUser(1L, updatedDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDetails.getUsername(), Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    void updateUser_shouldReturnNotFound_whenUserDoesNotExist() {
        when(userService.readOne(anyLong())).thenReturn(Optional.empty());

        User updatedDetails = new User();
        ResponseEntity<User> response = userController.updateUser(1L, updatedDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUser_shouldReturnNoContent_whenUserExists() {
        when(userService.readOne(anyLong())).thenReturn(Optional.of(new User()));
        doNothing().when(userService).delete(anyLong());

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUser_shouldReturnNotFound_whenUserDoesNotExist() {
        when(userService.readOne(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
