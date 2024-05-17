package com.example.equinosappapi.controllers;

import com.example.equinosappapi.models.Caballo;
import com.example.equinosappapi.services.CaballoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caballos")
public class RestControllerCaballos {

    private CaballoService caballoService;

    @Autowired
    public RestControllerCaballos(CaballoService caballoService) {
        this.caballoService = caballoService;
    }

    @PostMapping
    public void uploadCaballo(@RequestBody Caballo caballo) {
        caballoService.add(caballo);
    }

    @GetMapping
    public List<Caballo> listarCaballos() {
        return caballoService.readAll();
    }
}
