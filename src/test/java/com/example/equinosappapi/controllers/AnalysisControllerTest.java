package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.AnalysisDto;
import com.example.equinosappapi.models.Analysis;
import com.example.equinosappapi.models.Horse;
import com.example.equinosappapi.models.User;
import com.example.equinosappapi.services.AnalysisService;
import com.example.equinosappapi.services.HorseService;
import com.example.equinosappapi.services.ImageService;
import com.example.equinosappapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnalysisControllerTest {

    @Mock
    private AnalysisService analysisService;

    @Mock
    private HorseService horseService;

    @Mock
    private UserService userService;

    @Mock
    private ImageService imageService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AnalysisController analysisController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadAnalysis_shouldAddNewAnalysis() throws IOException {
        AnalysisDto analysisDto = new AnalysisDto();
        analysisDto.setHorseId(1L);
        analysisDto.setUserId(1L);
        analysisDto.setDisgustado(0.2f);
        analysisDto.setSereno(0.3f);
        analysisDto.setInteresado(0.5f);
        analysisDto.setPrediction("SERENO");

        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test-image".getBytes());

        when(horseService.getById(analysisDto.getHorseId())).thenReturn(new Horse());
        when(userService.getById(analysisDto.getUserId())).thenReturn(new User());

        analysisController.uploadAnalysis(analysisDto, image);

        verify(analysisService, times(1)).add(any(Analysis.class));
    }

    @Test
    void getAnalysisById_shouldReturnAnalysis() {
        Long analysisId = 1L;
        Analysis analysis = new Analysis();
        when(analysisService.getById(analysisId)).thenReturn(analysis);

        Analysis result = analysisController.getAnalysisById(analysisId);

        assertEquals(analysis, result);
        verify(analysisService, times(1)).getById(analysisId);
    }

    @Test
    void updateAnalysis_shouldReturnUpdatedAnalysis() throws IOException {
        Long analysisId = 1L;
        Analysis existingAnalysis = new Analysis();
        when(analysisService.readOne(analysisId)).thenReturn(Optional.of(existingAnalysis));

        AnalysisDto analysisDto = new AnalysisDto();
        analysisDto.setHorseId(1L);
        analysisDto.setUserId(1L);
        analysisDto.setDisgustado(0.2f);
        analysisDto.setSereno(0.3f);
        analysisDto.setInteresado(0.5f);
        analysisDto.setPrediction("SERENO");

        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test-image".getBytes());

        ResponseEntity<Analysis> response = analysisController.updateAnalysis(analysisId, analysisDto, image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(analysisService, times(1)).update(any(Analysis.class));
    }

    @Test
    void updateAnalysis_shouldReturnNotFoundWhenAnalysisDoesNotExist() throws IOException {
        Long analysisId = 1L;
        when(analysisService.readOne(analysisId)).thenReturn(Optional.empty());

        AnalysisDto analysisDto = new AnalysisDto();
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test-image".getBytes());

        ResponseEntity<Analysis> response = analysisController.updateAnalysis(analysisId, analysisDto, image);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(analysisService, never()).update(any(Analysis.class));
    }

    @Test
    void deleteAnalysis_shouldReturnNoContentWhenAnalysisExists() {
        Long analysisId = 1L;
        when(analysisService.readOne(analysisId)).thenReturn(Optional.of(new Analysis()));

        ResponseEntity<Void> response = analysisController.deleteAnalysis(analysisId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(analysisService, times(1)).delete(analysisId);
    }

    @Test
    void deleteAnalysis_shouldReturnNotFoundWhenAnalysisDoesNotExist() {
        Long analysisId = 1L;
        when(analysisService.readOne(analysisId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = analysisController.deleteAnalysis(analysisId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(analysisService, never()).delete(analysisId);
    }
}
