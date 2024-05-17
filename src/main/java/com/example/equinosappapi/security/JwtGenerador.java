package com.example.equinosappapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerador {

    // Metodo para generar un token por medio de la authenticacion
    public String generarToken(Authentication authentication) {
        String username = authentication.getName();
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        // Generar token
        String token = Jwts.builder() // Construimos un token JWT llamado token
                .setSubject(username) // Establecemos el nombre de usuario que esta iniciando sesion
                .setIssuedAt(new Date()) // Establecemos la fecha de emision del token en el momento actual
                .setExpiration(expiracionToken) // Establecemos la fecha de caducidad del token
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA) // Firmamos el token para evitar manipulacion
                .compact(); // Finalizamos la construccion y lo comvertimos en una cadena compacta
        return token;
    }

    // Metodo para extraer un username a partir de un token
    public String obtenerUsernameDeJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA) // Establece la clave de firma
                .parseClaimsJws(token)// Verifica la firma del token a partir del string token
                .getBody(); // Obtenemos el claims(cuerpo) ya verificado del token el cual contendra la informacion de username, fecha expiracion y firma del token
        return claims.getSubject(); // Retornamos el nombre de usuario
    }

    // Metood para validar el token
    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("El token ha expirado o es incorrecto.");
        }

    }
}
