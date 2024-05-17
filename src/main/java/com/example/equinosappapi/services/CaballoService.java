package com.example.equinosappapi.services;

import com.example.equinosappapi.repositories.ICaballoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
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

    public List<Caballo> readAll() {
        return caballoRepository.findAll();
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
}