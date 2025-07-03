package com.event.service;


import com.event.entity.User;

public interface UserService {
    User register(User user);
    User login(String email, String password);
    User update(Long userId, User userDetails);
}
