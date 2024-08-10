package com.example.equinosappapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorseDto {
    private Long id;
    private String name;
    private String gender;
    private String dateOfBirth;
    private boolean entrenamiento;
    private boolean estabulacion;
    private boolean salidaAPiquete;
    private boolean dolor;
    private byte[] image;
    private String observations;
}
