package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.HorseDto;
import com.example.equinosappapi.dtos.HorseWithCompressedImageDto;
import com.example.equinosappapi.models.Horse;
import com.example.equinosappapi.services.HorseService;
import com.example.equinosappapi.utils.ImageCompressor;
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
@RequestMapping("/api/horses")
public class HorseController {

    private final HorseService horseService;

    @Autowired
    public HorseController(HorseService horseService) {
        this.horseService = horseService;
    }

    @Operation(summary = "Cargar caballo")
    @PostMapping
    public void loadHorse(@RequestPart("horse") HorseDto horse, @RequestPart("image") MultipartFile image) throws IOException {
        Horse newHorse = new Horse();
        setHorseData(horse, newHorse);
        byte[] bytes = image.getBytes();
        newHorse.setImage(bytes);

        horseService.add(newHorse);

        Thread thread = new Thread(() -> {
            ImageCompressor compressor = new ImageCompressor();
            try {
                byte[] compressImage = compressor.compressImage(bytes);
                newHorse.setCompressedImage(compressImage);
                horseService.update(newHorse);
                System.out.println("Imagen comprimida y caballo actualizados exitosamente");
            } catch (IOException e) {
                System.out.println("Error al comprimir la imagen: " + e.getMessage());
            }
        });
        thread.start();
    }

    @Operation(summary = "Modificar caballo")
    @PreAuthorize("hasRole('ADVANCED_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<Horse> updateHorse(@PathVariable Long id, @RequestPart("horse") HorseDto horseDetails, @RequestPart("image") MultipartFile image) throws IOException {
        Optional<Horse> optionalHorse = horseService.readOne(id);
        if (optionalHorse.isPresent()) {
            Horse horse = optionalHorse.get();
            setHorseData(horseDetails, horse);
            byte[] bytes;

            if (image != null && !image.isEmpty()) {
                bytes = image.getBytes();
            } else {
                bytes = optionalHorse.get().getImage();
            }

            horse.setImage(bytes);
            ImageCompressor compressor = new ImageCompressor();
            byte[] compressImage = compressor.compressImage(bytes);
            horse.setCompressedImage(compressImage);

            horseService.update(horse);
            return ResponseEntity.ok(horse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private void setHorseData(@RequestPart("horse") HorseDto horseDetails, Horse horse) {
        horse.setName(horseDetails.getName());
        horse.setSexo(Horse.Gender.fromString(horseDetails.getGender()));
        horse.setDateOfBirth(horseDetails.getDateOfBirth());
        horse.setEntrenamiento(horseDetails.isEntrenamiento());
        horse.setEstabulacion(horseDetails.isEstabulacion());
        horse.setSalidaAPiquete(horseDetails.isSalidaAPiquete());
        horse.setDolor(horseDetails.isDolor());
        horse.setObservations(horseDetails.getObservations());
    }

    @Operation(summary = "Eliminar caballo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHorse(@PathVariable Long id) {
        Optional<Horse> horse = horseService.readOne(id);
        if (horse.isPresent()) {
            horseService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener caballos")
    @GetMapping
    public List<HorseWithCompressedImageDto> readAll() {
        return horseService.readAll();
    }

    @Operation(summary = "Obtener caballo por identificacion")
    @GetMapping("/{id}")
    public Horse getHorseById(@PathVariable Long id) {
        return horseService.getById(id);
    }
}
