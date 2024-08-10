package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.HorseDto;
import com.example.equinosappapi.dtos.HorseWithCompressedImageDto;
import com.example.equinosappapi.models.Horse;
import com.example.equinosappapi.services.HorseService;
import com.example.equinosappapi.utils.ImageCompressor;
import org.testng.annotations.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HorseControllerTest {

    @Mock
    private HorseService horseService;

    @InjectMocks
    private HorseController horseController;

    private MockMvc mockMvc;

    @Test
    void testLoadHorse() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(horseController).build();

        HorseDto horseDto = new HorseDto();
        horseDto.setName("Test Horse");
        horseDto.setGender("MALE");
        horseDto.setDateOfBirth("2023-01-01");
        horseDto.setEntrenamiento(true);
        horseDto.setEstabulacion(true);
        horseDto.setSalidaAPiquete(false);
        horseDto.setDolor(false);
        horseDto.setObservations("No observations");

        MockMultipartFile caballo = new MockMultipartFile("caballo", "", "application/json", "{\"name\": \"Test Horse\"}".getBytes());
        MockMultipartFile imagen = new MockMultipartFile("imagen", "horse.jpg", "image/jpeg", "some image".getBytes());

        mockMvc.perform(multipart("/api/caballos")
                        .file(caballo)
                        .file(imagen)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        ArgumentCaptor<Horse> horseArgumentCaptor = ArgumentCaptor.forClass(Horse.class);
        verify(horseService).add(horseArgumentCaptor.capture());

        Horse capturedHorse = horseArgumentCaptor.getValue();
        assertEquals("Test Horse", capturedHorse.getName());
        assertEquals(Horse.Gender.MALE, capturedHorse.getSexo());
    }

    @Test
    void testUpdateHorse() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(horseController).build();

        Long horseId = 1L;
        HorseDto horseDto = new HorseDto();
        horseDto.setName("Updated Horse");
        horseDto.setGender("FEMALE");
        horseDto.setDateOfBirth("2022-01-01");
        horseDto.setEntrenamiento(false);
        horseDto.setEstabulacion(false);
        horseDto.setSalidaAPiquete(true);
        horseDto.setDolor(true);
        horseDto.setObservations("Updated observations");

        MockMultipartFile caballo = new MockMultipartFile("caballo", "", "application/json", "{\"name\": \"Updated Horse\"}".getBytes());
        MockMultipartFile imagen = new MockMultipartFile("imagen", "updated-horse.jpg", "image/jpeg", "updated image".getBytes());

        Horse existingHorse = new Horse();
        when(horseService.readOne(horseId)).thenReturn(Optional.of(existingHorse));
        ImageCompressor compressor = mock(ImageCompressor.class);
        when(compressor.compressImage(any(byte[].class))).thenReturn(new byte[0]);

        mockMvc.perform(multipart("/api/caballos/" + horseId)
                        .file(caballo)
                        .file(imagen)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(horseService).update(existingHorse);
        assertEquals("Updated Horse", existingHorse.getName());
    }

    @Test
    void testDeleteHorse() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(horseController).build();

        Long horseId = 1L;
        when(horseService.readOne(horseId)).thenReturn(Optional.of(new Horse()));

        mockMvc.perform(delete("/api/caballos/" + horseId))
                .andExpect(status().isNoContent());

        verify(horseService).delete(horseId);
    }

    @Test
    void testDeleteHorseNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(horseController).build();

        Long horseId = 1L;
        when(horseService.readOne(horseId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/caballos/" + horseId))
                .andExpect(status().isNotFound());

        verify(horseService, never()).delete(horseId);
    }

    @Test
    void testReadAll() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(horseController).build();

        HorseWithCompressedImageDto horseDto = new HorseWithCompressedImageDto();
        when(horseService.readAll()).thenReturn(Collections.singletonList(horseDto));

        mockMvc.perform(get("/api/caballos"))
                .andExpect(status().isOk());

        verify(horseService).readAll();
    }

    @Test
    void testGetHorseById() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(horseController).build();

        Long horseId = 1L;
        Horse horse = new Horse();
        when(horseService.getById(horseId)).thenReturn(horse);

        mockMvc.perform(get("/api/caballos/" + horseId))
                .andExpect(status().isOk());

        verify(horseService).getById(horseId);
    }

    @Test
    void testGetHorseByIdNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(horseController).build();

        Long horseId = 1L;
        when(horseService.getById(horseId)).thenReturn(null);

        mockMvc.perform(get("/api/caballos/" + horseId))
                .andExpect(status().isNotFound());

        verify(horseService).getById(horseId);
    }
}
