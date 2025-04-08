package com.example.bankingapp.controller;

import com.example.bankingapp.dto.UpdatePasswordRequest;
import com.example.bankingapp.dto.UserResponse;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserProfile(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUserProfile(userId, updatedUser);

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId, @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }
}