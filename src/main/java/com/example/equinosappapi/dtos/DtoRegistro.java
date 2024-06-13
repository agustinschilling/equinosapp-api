package com.example.equinosappapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DtoRegistro { // Data transfer object
    @NotBlank(message = "Nombre de usuario no puede estar en blanco")
    @NotNull(message = "Nombre de usuario no puede ser nulo")
    private String username;

    @NotBlank(message = "Email de usuario no puede estar en blanco")
    @NotNull(message = "Email de usuario no puede ser nulo")
    @Email(message = "Debe ser un Email valido")
    private String email;

    @NotBlank(message = "La contrasenia no puede estar en blanco")
    @NotNull(message = "La contrasenia no puede ser nula")
    private String password;
}
