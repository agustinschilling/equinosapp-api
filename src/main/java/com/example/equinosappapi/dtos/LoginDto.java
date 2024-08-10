package com.example.equinosappapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "El campo identificacion no puede estar en blanco")
    @NotNull(message = "El campo identificacion no puede ser nulo")
    private String identification; // username o email

    @NotBlank(message = "El campo contrasenia no puede estar en blanco")
    @NotNull(message = "El campo contrasenia no puede ser nulo")
    private String password;
}