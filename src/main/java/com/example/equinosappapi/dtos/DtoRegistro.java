package com.example.equinosappapi.dtos;

import lombok.Data;

@Data
public class DtoRegistro { // Data transfer object
    private String username;
    private String email;
    private String password;
}
