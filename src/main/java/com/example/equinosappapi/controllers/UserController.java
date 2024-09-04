package com.example.equinosappapi.controllers;

import com.example.equinosappapi.models.User;
import com.example.equinosappapi.services.ImageService;
import com.example.equinosappapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.example.equinosappapi.security.JwtGenerator.getUsernameFromJwt;
import static com.example.equinosappapi.services.ImageService.getImageExtension;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final ImageService imageService;

    @Autowired
    public UserController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @Operation(summary = "Obtener usuario por identificacion")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.readOne(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Obtener usuario por nombre de usuario")
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Modificar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userService.readOne(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setRole(userDetails.getRole());
            userService.update(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.readOne(id);
        if (user.isPresent()) {
            userService.delete(id);
            String imagePath = user.get().getImage();
            if (imagePath != null) {
                imageService.deleteImage("users" + File.separator + imagePath);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Establecer imagen perfil de usuario")
    @PostMapping("/image")
    public ResponseEntity<Void> uploadProfileImage(@RequestPart("image") MultipartFile image, HttpServletRequest request) throws IOException {
        String token = UserService.extractTokenFromRequest(request);
        String username = getUsernameFromJwt(token);
        User user = userService.getByUsername(username);

        String userImage = user.getImage();
        if (userImage != null && !userImage.isEmpty()) {
            imageService.deleteImage("/users/" + userImage);
        }

        String originalFileName = image.getOriginalFilename();
        String extension = getImageExtension(originalFileName);
        byte[] bytes = image.getBytes();
        String imageName = imageService.saveImage(bytes, extension, "users");
        user.setImage(imageName);
        userService.update(user);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
