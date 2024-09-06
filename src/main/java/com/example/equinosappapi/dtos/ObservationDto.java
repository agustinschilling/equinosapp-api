package com.example.equinosappapi.dtos;

import com.example.equinosappapi.models.PredictionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObservationDto {
    private Long analysisId;
    private String observation;
    private PredictionEnum predictionEnum;
}
