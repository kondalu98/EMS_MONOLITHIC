package com.event.service;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.event.entity.User;
import com.event.exception.InvalidCredentialsException;
import com.event.exception.UserAlreadyExistsException;
import com.event.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");
    }

    @Test
    void testRegister_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User savedUser = userService.register(user);
        assertNotNull(savedUser);
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository).save(savedUser);
    }

    @Test
    void testRegister_UserAlreadyExists() {
        // Given email already exists
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));
    }

    @Test
    void testLogin_Success() {
        User dbUser = new User();
        dbUser.setEmail("test@example.com");
        dbUser.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(dbUser));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        User loggedInUser = userService.login("test@example.com", "plainPassword");

        assertNotNull(loggedInUser);
        assertEquals("test@example.com", loggedInUser.getEmail());
    }

    @Test
    void testLogin_InvalidCredentials() {
        User dbUser = new User();
        dbUser.setEmail("test@example.com");
        dbUser.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(dbUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () ->
                userService.login("test@example.com", "wrongPassword"));
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("sample@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () ->
                userService.login("sample@example.com", "password"));
    }
    @Test
    void testUpdate_Success() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old Name");
        existingUser.setPassword("oldPassword");
        existingUser.setContactNumber("1234567890");

        User updatedDetails = new User();
        updatedDetails.setName("New Name");
        updatedDetails.setPassword("newPassword");
        updatedDetails.setContactNumber("9876543210");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.update(1L, updatedDetails);

        assertEquals("New Name", result.getName());
        assertEquals("encodedNewPassword", result.getPassword());
        assertEquals("9876543210", result.getContactNumber());

        verify(userRepository).save(result);
    }

}

