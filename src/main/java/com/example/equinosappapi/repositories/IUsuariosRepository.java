package com.example.equinosappapi.repositories;

import com.example.equinosappapi.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuariosRepository extends JpaRepository<Usuario, Long> {
    // Buscar usuario mediante username
    Optional<Usuario> findByUsername(String username);

    // Check existencia mediante username
    Boolean existsByUsername(String username);
}
