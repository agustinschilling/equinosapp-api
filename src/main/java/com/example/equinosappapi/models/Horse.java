package com.example.equinosappapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "horse")
public class Horse {

    public enum Gender {
        MALE, FEMALE;

        public static Gender fromString(String value) {
            if (value == null) {
                throw new IllegalArgumentException("El valor no puede ser nulo");
            }
            return switch (value.toLowerCase()) {
                case "male", "masculino" -> MALE;
                case "female", "femenino" -> FEMALE;
                default -> throw new IllegalArgumentException("Valor no válido: " + value);
            };
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horse_id")
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender sexo;
    private String dateOfBirth; // DD-MM-AAAA
    private boolean entrenamiento;
    private boolean estabulacion;
    private boolean salidaAPiquete;
    private boolean dolor;
    private String image;
    private String observations;
}