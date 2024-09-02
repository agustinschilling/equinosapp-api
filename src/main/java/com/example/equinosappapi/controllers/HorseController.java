package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.HorseDto;
import com.example.equinosappapi.models.Horse;
import com.example.equinosappapi.services.HorseService;
import com.example.equinosappapi.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.equinosappapi.services.ImageService.getImageExtension;

@RestController
@RequestMapping("/api/horses")
public class HorseController {

    private final HorseService horseService;

    private final ImageService imageService;

    @Autowired
    public HorseController(HorseService horseService, ImageService imageService) {
        this.horseService = horseService;
        this.imageService = imageService;
    }

    @Operation(summary = "Cargar caballo")
    @PostMapping
    public void loadHorse(@RequestPart("horse") HorseDto horse, @RequestPart("image") MultipartFile image) throws IOException {
        Horse newHorse = new Horse();
        setHorseData(horse, newHorse);
        String originalFileName = image.getOriginalFilename();
        String extension = getImageExtension(originalFileName);
        byte[] bytes = image.getBytes();
        String imageName = imageService.saveImage(bytes, extension, "horses");
        newHorse.setImage(imageName);
        horseService.add(newHorse);
    }

    @Operation(summary = "Modificar caballo")
    @PutMapping("/{id}")
    public ResponseEntity<Horse> updateHorse(@PathVariable Long id, @RequestPart("horse") HorseDto horseDetails, @RequestPart("image") MultipartFile image) throws IOException {
        Optional<Horse> optionalHorse = horseService.readOne(id);
        if (optionalHorse.isPresent()) {
            Horse horse = optionalHorse.get();
            setHorseData(horseDetails, horse);

            if (image != null && !image.isEmpty()) {
                // Check if the horse already has an image
                String existingImageName = horse.getImage();
                if (existingImageName != null && !existingImageName.isEmpty()) {
                    // Delete the existing image
                    imageService.deleteImage("horses/" + existingImageName);
                }

                // Save the new image
                String originalFileName = image.getOriginalFilename();
                String extension = ImageService.getImageExtension(originalFileName);
                String imageName = imageService.saveImage(image.getBytes(), extension, "horses");
                horse.setImage(imageName);
            }

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
            // Get the associated image name
            String imageName = horse.get().getImage();
            if (imageName != null && !imageName.isEmpty()) {
                // Delete the image from the filesystem
                imageService.deleteImage("horses/" + imageName);
            }

            // Delete the horse from the database
            horseService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @Operation(summary = "Obtener caballos")
    @GetMapping
    public ResponseEntity<List<HorseDto>> readAll() {
        List<HorseDto> horses = horseService.readAll();
        return ResponseEntity.ok(horses);
    }

    @Operation(summary = "Obtener caballo por identificación")
    @GetMapping("/{id}")
    public ResponseEntity<Horse> getHorseById(@PathVariable Long id) {
        Optional<Horse> horse = horseService.readOne(id);
        return horse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}