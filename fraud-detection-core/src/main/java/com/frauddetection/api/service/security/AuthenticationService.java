package com.frauddetection.api.service.security;

import com.frauddetection.api.dto.user.AuthenticatedUser;
import com.frauddetection.api.dto.user.UserCredentials;
import com.frauddetection.api.repository.entity.User;
import com.frauddetection.api.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class AuthenticationService {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class);

    @Inject
    UserCredentialsService userCredentialsService;

    @Inject
    UserRepository userRepository;

    @Inject
    JWTProvider jwtProvider;

    public Optional<AuthenticatedUser> authenticateUser(UserCredentials credentials) {
        User user = userRepository.findByUsername(credentials.username());

        if (user != null && userCredentialsService.validatePassword(user.getPassword(), credentials)) {
            Set<String> roles = Set.of(user.getRole());
            String token = jwtProvider.generateToken(credentials, roles);

            LOGGER.infof("User authenticated successfully: %s", credentials.username());
            return Optional.of(new AuthenticatedUser(credentials.username(), roles, token));
        } else {
            LOGGER.infof("User authentication failed for username: %s", credentials.username());
            return Optional.empty();
        }
    }
}
