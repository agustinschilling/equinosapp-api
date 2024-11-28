package com.example.equinosappapi.controllers;

import com.example.equinosappapi.dtos.AuthResponseDto;
import com.example.equinosappapi.dtos.LoginDto;
import com.example.equinosappapi.dtos.RegistryDto;
import com.example.equinosappapi.models.Role;
import com.example.equinosappapi.models.User;
import com.example.equinosappapi.repositories.IUserRepository;
import com.example.equinosappapi.security.JwtGenerator;
import com.example.equinosappapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.equinosappapi.security.JwtGenerator.getUsernameFromJwt;

@Validated
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          IUserRepository userRepository,
                          JwtGenerator jwtGenerator,
                          UserService userService,
                          ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Registrar usuario")
    @PostMapping("register")
    public ResponseEntity<ObjectNode> register(@Valid @RequestBody RegistryDto registryDto) {
        ResponseEntity<ObjectNode> BAD_REQUEST = checkExistencia(registryDto);
        if (BAD_REQUEST != null) return BAD_REQUEST;

        createUser(registryDto, Role.USER);

        ObjectNode response = objectMapper.createObjectNode();
        response.put("message", "Registro de usuario exitoso");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void createUser(RegistryDto registryDto, Role role) {
        User user = new User();
        user.setUsername(registryDto.getUsername());
        user.setEmail(registryDto.getEmail());
        user.setPassword(passwordEncoder.encode(registryDto.getPassword()));
        user.setRole(role);
        userRepository.save(user);
    }

    private ResponseEntity<ObjectNode> checkExistencia(RegistryDto registryDto) {
        ObjectNode response = objectMapper.createObjectNode();
        if (userRepository.existsByUsername(registryDto.getUsername())) {
            response.put("message", "El usuario ya existe, intenta con otro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(registryDto.getEmail())) {
            response.put("message", "El email ya existe, intenta con otro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Operation(summary = "Registrar usuario avanzado")
    @PostMapping("registerAdvanced")
    public ResponseEntity<ObjectNode> registrarAdvancedUser(@Valid @RequestBody RegistryDto registryDto) {
        ResponseEntity<ObjectNode> BAD_REQUEST = checkExistencia(registryDto);
        if (BAD_REQUEST != null) return BAD_REQUEST;

        createUser(registryDto, Role.ADVANCED_USER);

        ObjectNode response = objectMapper.createObjectNode();
        response.put("message", "Registro de usuario avanzado exitoso");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Iniciar sesion")
    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            String username = getUsernameFromIdentifier(loginDto.getIdentification());

            User user = userService.getByUsername(username);

            Authentication authentication = authenticateUser(username, loginDto.getPassword());
            String token = generateToken(authentication);

            AuthResponseDto authResponse = new AuthResponseDto();
            authResponse.setAccessToken(token);
            authResponse.setUsername(username);
            authResponse.setEmail(user.getEmail());
            authResponse.setRole(user.getRole());
            authResponse.setUserId(user.getUserId());
            authResponse.setImage(user.getImage());

            return ResponseEntity.ok(authResponse);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("Email no registrado");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @Operation(summary = "Cerrar sesión")
    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = UserService.extractTokenFromRequest(request);
        // TODO: Implementar la lógica de cierre de sesión
        return new ResponseEntity<>("Sesión cerrada con éxito", HttpStatus.OK);
    }

    private String getUsernameFromIdentifier(String identifier) {
        if (identifier.contains("@")) {
            String username = userRepository.getUsernameByEmail(identifier);
            if (username == null) {
                throw new UsernameNotFoundException("Email no registrado");
            }
            return username;
        } else {
            return identifier;
        }
    }

    @Operation(summary = "Renovar token")
    @GetMapping("renew-token")
    public ResponseEntity<AuthResponseDto> renewToken(HttpServletRequest request) {
        String token = UserService.extractTokenFromRequest(request);
        if (token != null && jwtGenerator.validateToken(token)) {
            Authentication authentication = jwtGenerator.getAuthentication(token);
            String newToken = jwtGenerator.generateToken(authentication);

            String username = getUsernameFromJwt(token);
            User user = userService.getByUsername(username);

            AuthResponseDto authResponse = new AuthResponseDto();
            authResponse.setAccessToken(newToken);
            authResponse.setUsername(username);
            authResponse.setEmail(user.getEmail());
            authResponse.setRole(user.getRole());
            authResponse.setUserId(user.getUserId());

            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    private Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private String generateToken(Authentication authentication) {
        return jwtGenerator.generateToken(authentication);
    }
}
