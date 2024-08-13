package com.example.equinosappapi.repositories;

import com.example.equinosappapi.models.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnalysisRepository extends JpaRepository<Analysis, Long> {
}