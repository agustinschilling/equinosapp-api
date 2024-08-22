package com.example.equinosappapi.services;

import com.example.equinosappapi.models.Analysis;
import com.example.equinosappapi.repositories.IAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalysisService {
    private final IAnalysisRepository analysisRepository;

    @Autowired
    public AnalysisService(IAnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    public void add(Analysis analysis) {
        analysisRepository.save(analysis);
    }

    public List<Analysis> readAll() {
        return analysisRepository.findAll();
    }

    public Optional<Analysis> readOne(Long id) {
        return analysisRepository.findById(id);
    }

    public void update(Analysis analysis) {
        analysisRepository.save(analysis);
    }

    public void delete(Long id) {
        analysisRepository.deleteById(id);
    }

    public Analysis getById(Long id) {
        return analysisRepository.getReferenceById(id);
    }

    public List<Analysis> getAll() {
        return analysisRepository.findAll();
    }
}