package com.example.equinosappapi.repositories;

import com.example.equinosappapi.models.Analisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnalisisRepository extends JpaRepository<Analisis, Long> {
}