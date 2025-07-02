package com.event.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import com.event.entity.User;
import com.event.exception.InvalidCredentialsException;
import com.event.exception.UserAlreadyExistsException;
import com.event.repo.UserRepository;

import org.junit.jupiter.api.BeforeEach;


import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;


import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");
    }

    @Test
    void testRegister_Success() {
        // Given email does not exist
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // When
        User savedUser = userService.register(user);

        // Then
        assertNotNull(savedUser);
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository).save(savedUser);
    }

    @Test
    void testRegister_UserAlreadyExists() {
        // Given email already exists
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));
    }

    @Test
    void testLogin_Success() {
        User dbUser = new User();
        dbUser.setEmail("test@example.com");
        dbUser.setPassword("encodedPassword");

        // Given: user exists, password matches
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(dbUser));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        // When
        User loggedInUser = userService.login("test@example.com", "plainPassword");

        // Then
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
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () ->
                userService.login("notfound@example.com", "password"));
    }
}

