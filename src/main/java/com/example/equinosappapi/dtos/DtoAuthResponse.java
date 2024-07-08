package com.example.equinosappapi.dtos;

import com.example.equinosappapi.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Esta clase se utiliza para retornar tipo y valor del token e informacion del usuario
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer ";
    private Long idUsuario;
    private Role role;
    private String email;
    private String username;
}
