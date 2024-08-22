package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.AnalysisDto;
import com.example.equinosappapi.models.Analysis;
import com.example.equinosappapi.models.PredictionDetail;
import com.example.equinosappapi.models.PredictionEnum;
import com.example.equinosappapi.services.AnalysisService;
import com.example.equinosappapi.services.HorseService;
import com.example.equinosappapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final HorseService horseService;
    private final UserService userService;

    @Autowired
    public AnalysisController(AnalysisService analysisService, HorseService horseService, UserService userService) {
        this.analysisService = analysisService;
        this.horseService = horseService;
        this.userService = userService;
    }

    @Operation(summary = "Modificar analisis")
    @PostMapping
    public void uploadAnalysis(@RequestPart("analysis") AnalysisDto analysis, @RequestPart("image") MultipartFile image) throws IOException {
        Analysis newAnalysis = new Analysis();
        newAnalysis.setHorse(horseService.getById(analysis.getHorseId()));
        newAnalysis.setUser(userService.getById(analysis.getUserId()));
        newAnalysis.setImage(image.getBytes());

        PredictionDetail predictionDetail = new PredictionDetail();
        predictionDetail.setDisgustado(analysis.getDisgustado());
        predictionDetail.setSereno(analysis.getSereno());
        predictionDetail.setInteresado(analysis.getInteresado());
        predictionDetail.setPrediction(PredictionEnum.fromString(analysis.getPrediction()));
        newAnalysis.setPredictionDetail(predictionDetail);
        newAnalysis.setObservations(analysis.getObservations());

        analysisService.add(newAnalysis);
    }

    @Operation(summary = "Obtener analisis")
    @GetMapping("/{id}")
    public Analysis getAnalysisById(@PathVariable Long id) {
        return analysisService.getById(id);
    }

    @Operation(summary = "Modificar analisis")
    @PreAuthorize("hasRole('ADVANCED_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<Analysis> updateAnalysis(@PathVariable Long id, @RequestPart("analysis") AnalysisDto analysisDetails, @RequestPart("image") MultipartFile image) throws IOException {
        Optional<Analysis> optionalAnalysis = analysisService.readOne(id);
        if (optionalAnalysis.isPresent()) {
            Analysis analysis = optionalAnalysis.get();
            analysis.setHorse(horseService.getById(analysisDetails.getHorseId()));
            analysis.setUser(userService.getById(analysisDetails.getUserId()));

            if (image != null && !image.isEmpty()) {
                analysis.setImage(image.getBytes());
            }

            PredictionDetail predictionDetail = new PredictionDetail();
            predictionDetail.setDisgustado(analysisDetails.getDisgustado());
            predictionDetail.setSereno(analysisDetails.getSereno());
            predictionDetail.setInteresado(analysisDetails.getInteresado());
            predictionDetail.setPrediction(PredictionEnum.fromString(analysisDetails.getPrediction()));
            analysis.setPredictionDetail(predictionDetail);

            analysis.setObservations(analysisDetails.getObservations());

            analysisService.update(analysis);
            return ResponseEntity.ok(analysis);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar analisis")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        Optional<Analysis> analysis = analysisService.readOne(id);
        if (analysis.isPresent()) {
            analysisService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener todos los análisis")
    @GetMapping
    public ResponseEntity<List<Analysis>> getAllAnalysis() {
        List<Analysis> analyses = analysisService.getAll();
        if (analyses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(analyses);
    }
}
