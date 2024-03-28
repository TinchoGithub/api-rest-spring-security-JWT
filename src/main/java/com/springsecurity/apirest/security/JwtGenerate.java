package com.springsecurity.apirest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerate {

    // Método para crear un token por medio de la authenticacion
    public String generateToken(Authentication authentication){
        String userName = authentication.getName();
        Date currentTime = new Date();
        Date tokenExpiration = new Date(currentTime.getTime() + ConstantSecurity.JWT_EXPIRATION_TOKEN);

        String token = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(tokenExpiration)
                .signWith(SignatureAlgorithm.HS512, ConstantSecurity.JWT_FIRMA)
                .compact();
        return token;
    }

    // Extraer UserName apartir de un token
    public String getUserNameFromJwt(String token){
        Claims claims = Jwts.parser()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // METODO PARA VALIDAR EL TOKKEN

    public Boolean valiDatetoken(String token){
        try{
            Jwts.parser().setSigningKey(ConstantSecurity.JWT_FIRMA).parseClaimsJws(token);
            return true;

        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT ah expirado o está incorrecto");

        }

    }
}


