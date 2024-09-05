package com.example.equinosappapi.dtos;

import com.example.equinosappapi.models.AnalysisObservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<AnalysisObservation> analisysObservations;
}