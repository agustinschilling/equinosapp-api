package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.DtoAuthResponse;
import com.example.equinosappapi.dtos.DtoLogin;
import com.example.equinosappapi.dtos.DtoRegistro;
import com.example.equinosappapi.models.Role;
import com.example.equinosappapi.models.Usuario;
import com.example.equinosappapi.repositories.IUsuariosRepository;
import com.example.equinosappapi.security.JwtGenerador;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth/")
public class RestControllerAuth {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final IUsuariosRepository usuariosRepository;
    private final JwtGenerador jwtGenerador;

    @Autowired
    public RestControllerAuth(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, IUsuariosRepository usuariosRepository, JwtGenerador jwtGenerador) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.usuariosRepository = usuariosRepository;
        this.jwtGenerador = jwtGenerador;
    }
    //Método para poder registrar usuarios con role "user"
    @Operation(summary = "registrar user")
    @PostMapping("register")
    public ResponseEntity<String> registrar(@RequestBody DtoRegistro dtoRegistro) {
        ResponseEntity<String> BAD_REQUEST = checkExistencia(dtoRegistro);
        if (BAD_REQUEST != null) return BAD_REQUEST;
        crearUsuario(dtoRegistro, Role.USUARIO);
        return new ResponseEntity<>("Registro de usuario exitoso", HttpStatus.OK);
    }

    private void crearUsuario(DtoRegistro dtoRegistro, Role role) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dtoRegistro.getUsername());
        usuario.setEmail(dtoRegistro.getEmail());
        usuario.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        usuario.setRole(role);
        usuariosRepository.save(usuario);
    }

    private ResponseEntity<String> checkExistencia(DtoRegistro dtoRegistro) {
        if (usuariosRepository.existsByUsername(dtoRegistro.getUsername())) {
            return new ResponseEntity<>("El usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        if (usuariosRepository.existsByEmail(dtoRegistro.getEmail())) {
            return new ResponseEntity<>("El email ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    //Método para poder guardar usuarios de tipo ADMIN
    @PostMapping("registerVet")
    public ResponseEntity<String> registrarAdmin(@RequestBody DtoRegistro dtoRegistro) {
        ResponseEntity<String> BAD_REQUEST = checkExistencia(dtoRegistro);
        if (BAD_REQUEST != null) return BAD_REQUEST;
        crearUsuario(dtoRegistro, Role.VETERINARIO);
        return new ResponseEntity<>("Registro de admin exitoso", HttpStatus.OK);
    }

    //Método para poder logear un usuario, ya sea por username o email y obtener un token
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody DtoLogin dtoLogin) {
        try {
            String username = getUsernameFromIdentifier(dtoLogin.getIdentificacion());
            Authentication authentication = authenticateUser(username, dtoLogin.getPassword());
            String token = generateToken(authentication);
            return ResponseEntity.ok(new DtoAuthResponse(token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("Email no registrado");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    private String getUsernameFromIdentifier(String identifier) {
        if (identifier.contains("@")) {
            String username = usuariosRepository.getUsernameByEmail(identifier);
            if (username == null) {
                throw new UsernameNotFoundException("Email no registrado");
            }
            return username;
        } else {
            return identifier;
        }
    }

    private Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, password));
    }

    private String generateToken(Authentication authentication) {
        return jwtGenerador.generarToken(authentication);
    }
}