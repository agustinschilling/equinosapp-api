package com.example.equinosappapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAnalisis {
    private Long idUsuario;
    private Long idCaballo;
    private double interesado;
    private double sereno;
    private double disgustado;
    private byte[] imagen;
    private String prediccion;
}
