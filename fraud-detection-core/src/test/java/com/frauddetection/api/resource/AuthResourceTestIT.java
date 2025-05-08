package com.frauddetection.api.resource;

import com.frauddetection.api.dto.user.UserCredentials;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusIntegrationTest
public class AuthResourceTestIT {

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password";


    @Test
    void testSuccessfulAuthentication() {
        UserCredentials credentials = new UserCredentials(TEST_USERNAME, TEST_PASSWORD);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

        String token = response.jsonPath().getString("token");
        assertNotNull(token);
        assert (token.split("\\.").length == 3);
    }

    @Test
    void testFailedAuthentication() {
        UserCredentials credentials = new UserCredentials("invalid-user", "wrong-password");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }
}
