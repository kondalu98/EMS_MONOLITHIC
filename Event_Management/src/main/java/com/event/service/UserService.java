package com.event.service;


import com.event.entity.User;

import java.util.List;

public interface UserService {
    User register(User user);
    User login(String email, String password);
    User update(Long userId, User userDetails);
    User findByEmail(String email);
    List<User> getAllUsers();

}
