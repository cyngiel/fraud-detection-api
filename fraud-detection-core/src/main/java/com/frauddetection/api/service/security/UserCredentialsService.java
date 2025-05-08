package com.frauddetection.api.service.security;

import com.frauddetection.api.dto.user.UserCredentials;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserCredentialsService {

    public boolean validatePassword(String password, UserCredentials credentials) {
        return BcryptUtil.matches(credentials.password(), password);
    }
}
