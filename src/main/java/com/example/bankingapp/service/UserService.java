package com.example.bankingapp.service;

import com.example.bankingapp.entity.User;

public interface UserService {
    User registerUser(User user);
    User updateUserProfile(Long userId, User user);
    void updatePassword(Long userId, String newPassword);
    User findByUsername(String username);
    User findById(Long userId);
}
