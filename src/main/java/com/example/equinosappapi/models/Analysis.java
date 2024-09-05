package com.example.equinosappapi.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "analysis")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "horse_id", referencedColumnName = "horse_id")
    private Horse horse;

    private String image;

    @Embedded
    @Column(name = "prediction_detail")
    private PredictionDetail predictionDetail;

    private String observations;

    @JsonIgnore
    @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AnalysisObservation> analysisObservation;

    public void addAnalysisObservation(AnalysisObservation analysisObservation) {
        this.analysisObservation.add(analysisObservation);
    }

    @JsonProperty("SERENO")
    public List<AnalysisObservation> getAnalysisObservationOfSerenoType() {
        return analysisObservation.stream()
                .filter(observation -> PredictionEnum.SERENO.equals(observation.getPrediction()))
                .collect(Collectors.toList());
    }

    @JsonProperty("DISGUSTADO")
    public List<AnalysisObservation> getAnalysisObservationOfDisgustadoType() {
        return analysisObservation.stream()
                .filter(observation -> PredictionEnum.DISGUSTADO.equals(observation.getPrediction()))
                .collect(Collectors.toList());
    }

    @JsonProperty("INTERESADO")
    public List<AnalysisObservation> getAnalysisObservationOfInteresadoType() {
        return analysisObservation.stream()
                .filter(observation -> PredictionEnum.INTERESADO.equals(observation.getPrediction()))
                .collect(Collectors.toList());
    }
}