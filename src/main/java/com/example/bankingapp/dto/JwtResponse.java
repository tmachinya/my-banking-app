package com.example.bankingapp.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private List<String> roles;

}