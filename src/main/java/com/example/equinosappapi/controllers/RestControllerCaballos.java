package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.DtoCaballo;
import com.example.equinosappapi.dtos.DtoCaballoImagenComprimida;
import com.example.equinosappapi.models.Caballo;
import com.example.equinosappapi.services.CaballoService;
import com.example.equinosappapi.utils.ImageCompressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/caballos")
public class RestControllerCaballos {

    private final CaballoService caballoService;

    @Autowired
    public RestControllerCaballos(CaballoService caballoService) {
        this.caballoService = caballoService;
    }

    @PostMapping
    public void cargarCaballo(@RequestPart("caballo") DtoCaballo caballo, @RequestPart("imagen") MultipartFile imagen) throws IOException {
        Caballo newCaballo = new Caballo();
        newCaballo.setNombre(caballo.getNombre());
        newCaballo.setSexo(Caballo.Sexo.valueOf(caballo.getSexo()));
        newCaballo.setFechaNacimiento(caballo.getFechaNacimiento());
        newCaballo.setEntrenamiento(caballo.isEntrenamiento());
        newCaballo.setEstabulacion(caballo.isEstabulacion());
        newCaballo.setSalidaAPiquete(caballo.isSalidaAPiquete());
        newCaballo.setDolor(caballo.isDolor());
        byte[] bytes = imagen.getBytes();
        newCaballo.setImagen(bytes);
        newCaballo.setObservaciones(caballo.getObservaciones());

        caballoService.add(newCaballo);

        // Ejecutar la compresión en otro thread
        Thread thread = new Thread(() -> {
            ImageCompressor compressor = new ImageCompressor();
            try {
                byte[] imagenComprimida = compressor.comprimirImagen(bytes);
                newCaballo.setImagenComprimida(imagenComprimida);
                caballoService.update(newCaballo); // Actualizar el caballo con la imagen comprimida
                System.out.println("Imagen comprimida y caballo actualizados exitosamente");
            } catch (IOException e) {
                System.out.println("Error al comprimir la imagen: " + e.getMessage());
            }
        });
        thread.start();
    }

    @GetMapping
    public List<DtoCaballoImagenComprimida> readAll() {
        return caballoService.readAll();
    }
}
