package com.example.equinosappapi.controllers;

import com.example.equinosappapi.models.Caballo;
import com.example.equinosappapi.services.CaballoService;
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
    public void cargarCaballo(@RequestPart("caballo") Caballo caballo, @RequestPart("imagen") MultipartFile imagen) throws IOException {
        caballo.setImagen(imagen.getBytes());
        caballoService.add(caballo);
    }

    @GetMapping
    public List<Caballo> listarCaballos() {
        return caballoService.readAll();
    }
}
