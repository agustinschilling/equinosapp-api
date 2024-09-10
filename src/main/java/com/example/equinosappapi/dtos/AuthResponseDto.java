package com.example.equinosappapi.dtos;

import com.example.equinosappapi.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";
    private Long userId;
    private Role role;
    private String email;
    private String username;
    private String image;
}