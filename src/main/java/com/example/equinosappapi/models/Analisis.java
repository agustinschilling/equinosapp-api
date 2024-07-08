package com.example.equinosappapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "analisis")
public class Analisis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_analisis")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_caballo", referencedColumnName = "id_caballo")
    private Caballo caballo;

    @Lob
    private byte[] imagen;

    @Embedded
    @Column(name = "prediccion_detalle")
    private PrediccionDetalle prediccionDetalle;
}
