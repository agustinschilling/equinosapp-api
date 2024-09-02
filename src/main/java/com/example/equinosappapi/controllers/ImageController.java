package com.example.equinosappapi.controllers;

import com.example.equinosappapi.services.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/images/")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/**")
    public ResponseEntity<Resource> getImage(HttpServletRequest request) {
        // Extract the full path after /api/images/
        String fullPath = request.getRequestURI().substring(request.getContextPath().length() + "/api/images/".length());
        try {
            Resource image = imageService.getImage(fullPath);

            // Determine the content type based on the file extension
            String contentType = request.getServletContext().getMimeType(image.getFile().getAbsolutePath());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(image);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found", e);
        }
    }
}
