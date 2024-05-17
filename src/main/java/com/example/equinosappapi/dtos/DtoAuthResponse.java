package com.example.equinosappapi.dtos;

import lombok.Data;

// Esta clase es la que devuelve la info con el token y el tipo de este
@Data
public class DtoAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer ";

    public DtoAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
