package com.example.equinosappapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoCaballoImagenComprimida {
    private Long id;
    private String nombre;
    private String sexo;
    private String fechaNacimiento;
    private boolean entrenamiento;
    private boolean estabulacion;
    private boolean salidaAPiquete;
    private boolean dolor;
    private byte[] imagenComprimida;
    private String observaciones;
}
