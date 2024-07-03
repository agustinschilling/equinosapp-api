package com.example.equinosappapi.services;

import com.example.equinosappapi.models.Analisis;
import com.example.equinosappapi.repositories.IAnalisisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalisisService {
    private IAnalisisRepository analisisRepository;

    @Autowired
    public AnalisisService(IAnalisisRepository analisisRepository) {
        this.analisisRepository = analisisRepository;
    }

    public void add(Analisis analisis) {
        analisisRepository.save(analisis);
    }

    public List<Analisis> readAll() {
        return analisisRepository.findAll();
    }

    public Optional<Analisis> readOne(Long id) {
        return analisisRepository.findById(id);
    }

    public void update(Analisis analisis) {
        analisisRepository.save(analisis);
    }

    public void delete(Long id) {
        analisisRepository.deleteById(id);
    }

    public Analisis getById(Long id) {
        return analisisRepository.getReferenceById(id);
    }
}