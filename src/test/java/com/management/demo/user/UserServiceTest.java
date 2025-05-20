package com.management.demo.user;

import com.management.demo.user.exceptions.UserConflictException;
import com.management.demo.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Ativa o Mockito para os testes
class UserServiceTest {

    @Mock
    private IUserRepository userRepository; // Mock do repositório

    @InjectMocks
    private UserService userService; // Service real com mocks injetados

    private UserDTO inputDTO;   // DTO usado para criar User
    private User savedUser;     // User salvo simulado
    private UUID generatedId;   // UUID comum para todos os testes

    @BeforeEach
    void setUp() {
        // Preparamos o que é comum a todos os testes
        generatedId = UUID.randomUUID();

        inputDTO = new UserDTO(null, "John Doe", "john@example.com");

        savedUser = new User("John Doe", "john@example.com", new ArrayList<>());
        savedUser.setId(generatedId);
    }

    @Test
    void createUser_ShouldReturnUserDTO_WhenUserDoesNotExist() {
        // GIVEN
        when(userRepository.existsByEmail(inputDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // WHEN
        UserDTO result = userService.createUser(inputDTO);

        // THEN
        assertNotNull(result);
        assertEquals(generatedId, result.getId());
        assertEquals(inputDTO.getName(), result.getName());
        assertEquals(inputDTO.getEmail(), result.getEmail());

        verify(userRepository, times(1)).existsByEmail(inputDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // GIVEN
        when(userRepository.existsByEmail(inputDTO.getEmail())).thenReturn(true);

        // WHEN + THEN
        UserConflictException exception = assertThrows(
                UserConflictException.class,
                () -> userService.createUser(inputDTO)
        );

        assertTrue(exception.getMessage().contains(inputDTO.getEmail()));

        verify(userRepository, times(1)).existsByEmail(inputDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDTO_WhenUserExists() {
        // GIVEN
        User existingUser = new User("Old Name", "old@example.com", new ArrayList<>());
        existingUser.setId(generatedId);

        UserDTO updateDTO = new UserDTO(generatedId, "Updated Name", "updated@example.com");

        when(userRepository.findById(generatedId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        UserDTO result = userService.updateUser(updateDTO);

        // THEN
        assertNotNull(result);
        assertEquals(generatedId, result.getId());
        assertEquals(updateDTO.getName(), result.getName());
        assertEquals(updateDTO.getEmail(), result.getEmail());

        verify(userRepository, times(1)).findById(generatedId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        // GIVEN
        UserDTO updateDTO = new UserDTO(generatedId, "Any Name", "any@example.com");

        when(userRepository.findById(generatedId)).thenReturn(Optional.empty());

        // WHEN + THEN
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(updateDTO)
        );

        assertTrue(exception.getMessage().contains(generatedId.toString()));

        verify(userRepository, times(1)).findById(generatedId);
        verify(userRepository, never()).save(any(User.class));
    }
}