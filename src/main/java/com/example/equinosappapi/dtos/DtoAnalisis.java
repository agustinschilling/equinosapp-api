package com.example.equinosappapi.dtos;

import com.example.equinosappapi.models.Prediccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAnalisis {
    private Long idUsuario;
    private Long idCaballo;
    private byte[] imagen;
    private Prediccion prediccion;
}
