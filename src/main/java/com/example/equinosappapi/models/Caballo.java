package com.example.equinosappapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caballo")
public class Caballo {

    public enum Sexo {
        MASCULINO, FEMENINO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caballo")
    private Long id;
    private String nombre;
    private Sexo sexo;
    private String fechaNacimiento;
    private boolean entrenamiento;
    private boolean estabulacion;
    private boolean salidaAPiquete;
    private boolean dolor;
}
