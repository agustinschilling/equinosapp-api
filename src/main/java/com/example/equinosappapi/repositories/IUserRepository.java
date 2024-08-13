package com.example.equinosappapi.repositories;

import com.example.equinosappapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("SELECT u.username FROM User u WHERE u.email = ?1")
    String getUsernameByEmail(String email);

    Boolean existsByEmail(String email);

    User getByUsername(String username);
}
