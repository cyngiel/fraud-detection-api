package com.frauddetection.api.service.security;

import com.frauddetection.api.dto.user.UserCredentials;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserCredentialsService {

    /**
     * Validates the provided password against the stored hashed password.
     *
     * @param password    The plain text password to validate.
     * @param credentials The user credentials containing the hashed password.
     * @return true if the password matches, false otherwise.
     */
    public boolean validatePassword(String password, UserCredentials credentials) {
        return BcryptUtil.matches(credentials.password(), password);
    }
}
