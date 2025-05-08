package com.frauddetection.api.service.security;

import com.frauddetection.api.dto.user.UserCredentials;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class UserCredentialsServiceTest {

    private static final String storedPassword = "$2a$10$abcdefghijklmnopqrstuv";

    @InjectMocks
    private UserCredentialsService service;

    @Test
    void shouldReturnTrueWhenPasswordMatches() {
        try (MockedStatic<BcryptUtil> bcryptUtil = mockStatic(BcryptUtil.class)) {
            UserCredentials credentials = new UserCredentials("username", "password123");

            bcryptUtil.when(() -> BcryptUtil.matches(eq(credentials.password()), eq(storedPassword)))
                    .thenReturn(true);

            boolean result = service.validatePassword(storedPassword, credentials);

            assertTrue(result);
        }
    }

    @Test
    void shouldReturnFalseWhenPasswordDoesNotMatch() {
        try (MockedStatic<BcryptUtil> bcryptUtil = mockStatic(BcryptUtil.class)) {
            UserCredentials credentials = new UserCredentials("username", "wrongPassword");

            bcryptUtil.when(() -> BcryptUtil.matches(eq(credentials.password()), eq(storedPassword)))
                    .thenReturn(false);

            boolean result = service.validatePassword(storedPassword, credentials);

            assertFalse(result);
        }
    }
}
