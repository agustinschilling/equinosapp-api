package com.example.equinosappapi.repositories;

import com.example.equinosappapi.models.Caballo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICaballoRepository extends JpaRepository<Caballo, Long> {
}