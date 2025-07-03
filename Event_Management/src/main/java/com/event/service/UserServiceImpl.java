package com.event.service;

import com.event.entity.Event;
import com.event.entity.User;
import com.event.exception.EventNotFoundException;
import com.event.exception.InvalidCredentialsException;
import com.event.exception.UserAlreadyExistsException;
import com.event.exception.UserNotFoundException;
import com.event.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

    @Override
    public User update(Long userId, User userDetails) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        existingUser.setName(userDetails.getName());
        existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        existingUser.setContactNumber(userDetails.getContactNumber());

        return userRepository.save(existingUser);



    }


}
