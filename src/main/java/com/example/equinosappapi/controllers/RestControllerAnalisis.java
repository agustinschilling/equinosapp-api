package com.example.equinosappapi.controllers;

import com.example.equinosappapi.models.Analisis;
import com.example.equinosappapi.services.AnalisisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analisis")
public class RestControllerAnalisis {

    private AnalisisService analisisService;

    @Autowired
    public RestControllerAnalisis(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @PostMapping
    public void uploadAnalisis(@RequestBody Analisis analisis) {
        analisisService.add(analisis);
    }
}
