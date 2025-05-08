package com.frauddetection.api.resource;

import com.frauddetection.api.dto.user.AuthenticatedUser;
import com.frauddetection.api.dto.user.UserCredentials;
import com.frauddetection.api.service.security.AuthenticationService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class AuthResourceTest {

    @InjectMock
    AuthenticationService authenticationService;

    @Test
    void testLoginSuccess() {
        UserCredentials credentials = new UserCredentials("testuser", "password123");
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("testuser", Set.of("USER"), "JWT_TOKEN_VALUE");

        Mockito.when(authenticationService.authenticateUser(any(UserCredentials.class))).thenReturn(Optional.of(authenticatedUser));

        given()
            .contentType(ContentType.JSON)
            .body(credentials)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .body("token", is("JWT_TOKEN_VALUE"));
    }

    @Test
    void testLoginFailure() {
        UserCredentials credentials = new UserCredentials("invalid", "wrong");

        Mockito.when(authenticationService.authenticateUser(any(UserCredentials.class))).thenReturn(Optional.empty());

        given()
            .contentType(ContentType.JSON)
            .body(credentials)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(401);
    }
}