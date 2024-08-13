package com.example.equinosappapi.repositories;

import com.example.equinosappapi.models.Horse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHorseRepository extends JpaRepository<Horse, Long> {
}