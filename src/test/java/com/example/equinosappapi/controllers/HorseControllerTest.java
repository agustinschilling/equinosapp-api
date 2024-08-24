package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.HorseDto;
import com.example.equinosappapi.dtos.HorseWithCompressedImageDto;
import com.example.equinosappapi.models.Horse;
import com.example.equinosappapi.services.HorseService;
import com.example.equinosappapi.utils.ImageCompressor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HorseControllerTest {

    @Mock
    private HorseService horseService;

    @InjectMocks
    private HorseController horseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadHorse_shouldAddHorseAndStartThreadForImageCompression() throws IOException {
        // Arrange
        HorseDto horseDto = new HorseDto();
        horseDto.setName("Caballo 1");
        horseDto.setGender("MALE");
        horseDto.setDateOfBirth("2023-01-01");
        horseDto.setEntrenamiento(true);
        horseDto.setEstabulacion(true);
        horseDto.setSalidaAPiquete(true);
        horseDto.setDolor(false);
        horseDto.setObservations("Observaciones");

        MockMultipartFile image = new MockMultipartFile("image", "horse.jpg", "image/jpeg", "dummy image".getBytes());

        // Act
        horseController.loadHorse(horseDto, image);

        // Assert
        ArgumentCaptor<Horse> horseCaptor = ArgumentCaptor.forClass(Horse.class);
        verify(horseService).add(horseCaptor.capture());
        Horse addedHorse = horseCaptor.getValue();
        assertEquals("Caballo 1", addedHorse.getName());
        assertEquals(Horse.Gender.MALE, addedHorse.getSexo());
        assertEquals("2023-01-01", addedHorse.getDateOfBirth());
        assertArrayEquals(image.getBytes(), addedHorse.getImage());
        assertNull(addedHorse.getCompressedImage()); // Compressed image is set later in the thread
    }

    // @Test
    void updateHorse_shouldUpdateHorseWhenExists() throws IOException {
        // Arrange
        Long horseId = 1L;
        HorseDto horseDto = new HorseDto();
        horseDto.setName("Updated Name");
        horseDto.setGender("FEMALE");
        horseDto.setDateOfBirth("2020-01-01");
        horseDto.setEntrenamiento(false);
        horseDto.setEstabulacion(true);
        horseDto.setSalidaAPiquete(false);
        horseDto.setDolor(true);
        horseDto.setObservations("Updated Observations");

        MockMultipartFile image = new MockMultipartFile("image", "horse.jpg", "image/jpeg", "dummy image".getBytes());

        Horse existingHorse = new Horse();
        existingHorse.setId(horseId);

        when(horseService.readOne(horseId)).thenReturn(Optional.of(existingHorse));

        // Act
        ResponseEntity<Horse> response = horseController.updateHorse(horseId, horseDto, image);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", existingHorse.getName());
        assertEquals(Horse.Gender.FEMALE, existingHorse.getSexo());
        assertEquals("2020-01-01", existingHorse.getDateOfBirth());
        assertArrayEquals(image.getBytes(), existingHorse.getImage());
        verify(horseService).update(existingHorse);
    }

    @Test
    void updateHorse_shouldReturnNotFoundWhenHorseDoesNotExist() throws IOException {
        // Arrange
        Long horseId = 1L;
        HorseDto horseDto = new HorseDto();
        MockMultipartFile image = new MockMultipartFile("image", "horse.jpg", "image/jpeg", "dummy image".getBytes());

        when(horseService.readOne(horseId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Horse> response = horseController.updateHorse(horseId, horseDto, image);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(horseService, never()).update(any(Horse.class));
    }

    @Test
    void deleteHorse_shouldDeleteHorseWhenExists() {
        // Arrange
        Long horseId = 1L;
        Horse existingHorse = new Horse();
        existingHorse.setId(horseId);

        when(horseService.readOne(horseId)).thenReturn(Optional.of(existingHorse));

        // Act
        ResponseEntity<Void> response = horseController.deleteHorse(horseId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(horseService).delete(horseId);
    }

    @Test
    void deleteHorse_shouldReturnNotFoundWhenHorseDoesNotExist() {
        // Arrange
        Long horseId = 1L;

        when(horseService.readOne(horseId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = horseController.deleteHorse(horseId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(horseService, never()).delete(anyLong());
    }

    @Test
    void readAll_shouldReturnListOfHorses() {
        // Arrange
        List<HorseWithCompressedImageDto> horses = List.of(
                new HorseWithCompressedImageDto(
                        1L,
                        "Horse 1",
                        "MALE",
                        "2020-01-01",
                        true,
                        true,
                        true,
                        false,
                        new byte[]{},
                        "Observations 1"
                ),
                new HorseWithCompressedImageDto(
                        2L,
                        "Horse 2",
                        "FEMALE",
                        "2019-01-01",
                        false,
                        false,
                        false,
                        true,
                        new byte[]{},
                        "Observations 2"
                )
        );
        when(horseService.readAll()).thenReturn(horses);

        // Act
        List<HorseWithCompressedImageDto> result = horseController.readAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Horse 1", result.get(0).getName());
        assertEquals("Horse 2", result.get(1).getName());
        assertEquals("Observations 1", result.get(0).getObservations());
        assertEquals("Observations 2", result.get(1).getObservations());
    }


    @Test
    void getHorseById_shouldReturnHorseWhenExists() {
        // Arrange
        Long horseId = 1L;
        Horse horse = new Horse();
        horse.setId(horseId);
        when(horseService.getById(horseId)).thenReturn(horse);

        // Act
        Horse result = horseController.getHorseById(horseId);

        // Assert
        assertEquals(horseId, result.getId());
        verify(horseService).getById(horseId);
    }
}
