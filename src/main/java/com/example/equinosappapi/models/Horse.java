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
            switch (value.toLowerCase()) {
                case "male":
                case "masculino":
                    return MALE;
                case "female":
                case "femenino":
                    return FEMALE;
                default:
                    throw new IllegalArgumentException("Valor no válido: " + value);
            }
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
    @Lob
    private byte[] image;
    @Lob
    @Column(name = "compressed_image")
    private byte[] compressedImage;
    private String observations;
}
