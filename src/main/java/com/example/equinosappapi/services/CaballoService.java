package com.example.equinosappapi.services;

import com.example.equinosappapi.dtos.DtoCaballo;
import com.example.equinosappapi.dtos.DtoCaballoImagenComprimida;
import com.example.equinosappapi.repositories.ICaballoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.equinosappapi.models.Caballo;

@Service
public class CaballoService {
    private ICaballoRepository caballoRepository;

    @Autowired
    public CaballoService(ICaballoRepository caballoRepository) {
        this.caballoRepository = caballoRepository;
    }

    public void add(Caballo caballo) {
        caballoRepository.save(caballo);
    }

    public List<DtoCaballoImagenComprimida> readAll() {
        List<Caballo> caballos = caballoRepository.findAll();
        return caballos.stream().map(this::convertToDtoCaballoImagenComprimida).collect(Collectors.toList());
    }

    public Optional<Caballo> readOne(Long id) {
        return caballoRepository.findById(id);
    }

    public void update(Caballo analisis) {
        caballoRepository.save(analisis);
    }

    public void delete(Long id) {
        caballoRepository.deleteById(id);
    }

    private DtoCaballoImagenComprimida convertToDtoCaballoImagenComprimida(Caballo caballo) {
        return new DtoCaballoImagenComprimida(
                caballo.getId(),
                caballo.getNombre(),
                caballo.getSexo().toString(),
                caballo.getFechaNacimiento(),
                caballo.isEntrenamiento(),
                caballo.isEstabulacion(),
                caballo.isSalidaAPiquete(),
                caballo.isDolor(),
                caballo.getImagenComprimida(),
                caballo.getObservaciones()
        );
    }

    private DtoCaballo convertToDtoCaballo(Caballo caballo) {
        return new DtoCaballo(
                caballo.getId(),
                caballo.getNombre(),
                caballo.getSexo().toString(),
                caballo.getFechaNacimiento(),
                caballo.isEntrenamiento(),
                caballo.isEstabulacion(),
                caballo.isSalidaAPiquete(),
                caballo.isDolor(),
                caballo.getImagenComprimida(),
                caballo.getObservaciones()
        );
    }
}