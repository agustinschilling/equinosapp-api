package com.example.equinosappapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import static com.example.equinosappapi.security.SafetyConstants.JWT_SIGNATURE;
import static com.example.equinosappapi.security.SafetyConstants.JWT_EXPIRATION_TOKEN;

import java.util.Date;

@Component
public class JwtGenerator {

    private final UserDetailsService userDetailsService;

    public JwtGenerator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentTime = new Date();
        Date tokenExpirationDate = new Date(currentTime.getTime() + JWT_EXPIRATION_TOKEN);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentTime)
                .setExpiration(tokenExpirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SIGNATURE)
                .compact();
    }

    public static String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SIGNATURE)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SIGNATURE).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("El token ha expirado o es incorrecto.");
        }
    }

    public Authentication getAuthentication(String token) {
        String username = getUsernameFromJwt(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}