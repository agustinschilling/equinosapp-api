package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.DtoAnalisis;
import com.example.equinosappapi.models.Analisis;
import com.example.equinosappapi.services.AnalisisService;
import com.example.equinosappapi.services.CaballoService;
import com.example.equinosappapi.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/analisis")
public class RestControllerAnalisis {

    private final AnalisisService analisisService;
    private final CaballoService caballoService;
    private final UsuarioService usuarioService;

    @Autowired
    public RestControllerAnalisis(AnalisisService analisisService, CaballoService caballoService, UsuarioService usuarioService) {
        this.analisisService = analisisService;
        this.caballoService = caballoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public void uploadAnalisis(@RequestBody DtoAnalisis analisis, @RequestPart("imagen") MultipartFile imagen) throws IOException {
        Analisis newAnalisis = new Analisis();
        newAnalisis.setCaballo(caballoService.getById(analisis.getIdCaballo()));
        newAnalisis.setUsuario(usuarioService.getById(analisis.getIdUsuario()));
        newAnalisis.setImagen(imagen.getBytes());
        newAnalisis.setPrediccion(analisis.getPrediccion());

        analisisService.add(newAnalisis);
    }

    @GetMapping
    public Analisis getAnalisisById(Long id) {
        return analisisService.getById(id);
    }
}
