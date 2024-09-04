package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.AnalysisDto;
import com.example.equinosappapi.models.Analysis;
import com.example.equinosappapi.models.PredictionDetail;
import com.example.equinosappapi.models.PredictionEnum;
import com.example.equinosappapi.services.AnalysisService;
import com.example.equinosappapi.services.HorseService;
import com.example.equinosappapi.services.ImageService;
import com.example.equinosappapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.equinosappapi.services.ImageService.getImageExtension;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final HorseService horseService;
    private final UserService userService;
    private final ImageService imageService;


    @Autowired
    public AnalysisController(AnalysisService analysisService, HorseService horseService, UserService userService, ImageService imageService) {
        this.analysisService = analysisService;
        this.horseService = horseService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @Operation(summary = "Subir analisis")
    @PostMapping
    public void uploadAnalysis(@RequestPart("analysis") AnalysisDto analysis, @RequestPart("image") MultipartFile image) throws IOException {
        Analysis newAnalysis = new Analysis();
        setAnalysisData(analysis, newAnalysis);

        if (image != null && !image.isEmpty()) {
            String originalFileName = image.getOriginalFilename();
            String extension = getImageExtension(originalFileName);
            String imageName = imageService.saveImage(image.getBytes(), extension, "analysis");
            newAnalysis.setImage(imageName);
        }
        analysisService.add(newAnalysis);
    }

    private void setAnalysisData(@RequestPart("analysis") AnalysisDto analysis, Analysis newAnalysis) {
        newAnalysis.setHorse(horseService.getById(analysis.getHorseId()));
        newAnalysis.setUser(userService.getById(analysis.getUserId()));
        PredictionDetail predictionDetail = new PredictionDetail();
        predictionDetail.setDisgustado(analysis.getDisgustado());
        predictionDetail.setSereno(analysis.getSereno());
        predictionDetail.setInteresado(analysis.getInteresado());
        predictionDetail.setPrediction(PredictionEnum.fromString(analysis.getPrediction()));
        newAnalysis.setPredictionDetail(predictionDetail);
        newAnalysis.setObservations(analysis.getObservations());
    }

    @Operation(summary = "Obtener analisis")
    @GetMapping("/{id}")
    public Analysis getAnalysisById(@PathVariable Long id) {
        return analysisService.getById(id);
    }

    @Operation(summary = "Modificar analisis")
    @PutMapping("/{id}")
    public ResponseEntity<Analysis> updateAnalysis(@PathVariable Long id, @RequestPart("analysis") AnalysisDto analysisDetails, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        Optional<Analysis> optionalAnalysis = analysisService.readOne(id);
        if (optionalAnalysis.isPresent()) {
            Analysis analysis = optionalAnalysis.get();
            String oldImageName = analysis.getImage();

            setAnalysisData(analysisDetails, analysis);

            if (image != null && !image.isEmpty()) {
                String originalFileName = image.getOriginalFilename();
                String extension = getImageExtension(originalFileName);
                byte[] bytes = image.getBytes();
                String imageName = imageService.saveImage(bytes, extension, "analysis");
                analysis.setImage(imageName);

                if (oldImageName != null) {
                    imageService.deleteImage("analysis" + File.separator + oldImageName);
                }
            }

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
            String imageName = analysis.get().getImage();
            if (imageName != null) {
                imageService.deleteImage("analysis" + File.separator + imageName);
            }
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