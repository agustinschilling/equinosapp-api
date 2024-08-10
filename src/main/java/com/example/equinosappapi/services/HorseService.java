package com.example.equinosappapi.services;

import com.example.equinosappapi.dtos.HorseDto;
import com.example.equinosappapi.dtos.HorseWithCompressedImageDto;
import com.example.equinosappapi.repositories.IHorseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.equinosappapi.models.Horse;

@Service
public class HorseService {
    private final IHorseRepository horseRepository;

    @Autowired
    public HorseService(IHorseRepository horseRepository) {
        this.horseRepository = horseRepository;
    }

    public void add(Horse horse) {
        horseRepository.save(horse);
    }

    public List<HorseWithCompressedImageDto> readAll() {
        List<Horse> horses = horseRepository.findAll();
        return horses.stream().map(this::convertToHorseWithCompressedImageDto).collect(Collectors.toList());
    }

    public Optional<Horse> readOne(Long id) {
        return horseRepository.findById(id);
    }

    public void update(Horse horse) {
        horseRepository.save(horse);
    }

    public void delete(Long id) {
        horseRepository.deleteById(id);
    }

    public Horse getById(Long id) {
        return horseRepository.getReferenceById(id);
    }

    private HorseWithCompressedImageDto convertToHorseWithCompressedImageDto(Horse horse) {
        return new HorseWithCompressedImageDto(
                horse.getId(),
                horse.getName(),
                horse.getSexo().toString(),
                horse.getDateOfBirth(),
                horse.isEntrenamiento(),
                horse.isEstabulacion(),
                horse.isSalidaAPiquete(),
                horse.isDolor(),
                horse.getCompressedImage(),
                horse.getObservations()
        );
    }

    private HorseDto convertToHorseDto(Horse horse) {
        return new HorseDto(
                horse.getId(),
                horse.getName(),
                horse.getSexo().toString(),
                horse.getDateOfBirth(),
                horse.isEntrenamiento(),
                horse.isEstabulacion(),
                horse.isSalidaAPiquete(),
                horse.isDolor(),
                horse.getCompressedImage(),
                horse.getObservations()
        );
    }
}