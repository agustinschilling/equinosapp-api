package com.example.equinosappapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisDto {
    private Long userId;
    private Long horseId;
    private double interesado;
    private double sereno;
    private double disgustado;
    private byte[] image;
    private String prediction;
    private String observations;
}
