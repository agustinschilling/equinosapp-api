package com.example.equinosappapi.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PredictionDetail {
    private double interesado;
    private double sereno;
    private double disgustado;

    @Enumerated(EnumType.STRING)
    private PredictionEnum prediction;
}
