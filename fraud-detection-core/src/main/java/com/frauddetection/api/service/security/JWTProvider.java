package com.frauddetection.api.service.security;

import com.frauddetection.api.dto.user.UserCredentials;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;

@ApplicationScoped
public class JWTProvider {

    public String generateToken(UserCredentials credentials, Set<String> roles) {
        return Jwt.issuer("https://frauddetection.api.com")
                .upn(credentials.username())
                .groups(roles)
                .expiresIn(3600)
                .sign();
    }
}
