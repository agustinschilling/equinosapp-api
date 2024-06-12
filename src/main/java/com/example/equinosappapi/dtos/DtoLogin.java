package com.example.equinosappapi.dtos;

import lombok.Data;

@Data
public class DtoLogin {
    private String identificacion; // username o email
    private String password;
}