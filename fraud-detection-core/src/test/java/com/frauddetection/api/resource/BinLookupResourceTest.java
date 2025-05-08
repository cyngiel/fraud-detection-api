package com.frauddetection.api.resource;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.OK;


@QuarkusIntegrationTest
class BinLookupResourceIT {

    @Test
    void testGetBinDetailsSuccess() {
        // This assumes your deployed application has a mock implementation
        // or test configuration that returns this data for BIN 123456
        given()
                .contentType(ContentType.JSON)
                .queryParam("bin", 12345678)
                .auth().basic("testuser", "password")
                .when()
                .get("/bin-lookup")
                .then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    void testGetBinDetailsNotFound() {
        // Assuming 999999 is configured to return not found in test environment
        given()
                .contentType(ContentType.JSON)
                .queryParam("bin", 999999)
                .auth().basic("testuser", "password")
                .when()
                .get("/bin-lookup")
                .then()
                .statusCode(404);
    }

    @Test
    void testGetBinDetailsServerError() {
        // Assuming 500000 is configured to trigger an error in test environment
        given()
                .contentType(ContentType.JSON)
                .queryParam("bin", 500000)
                .auth().basic("testuser", "password")
                .when()
                .get("/bin-lookup")
                .then()
                .statusCode(500);
    }

    @Test
    void testGetBinDetailsValidationFailureTooSmall() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("bin", 99999) // Less than 6 digits
                .auth().basic("testuser", "password")
                .when()
                .get("/bin-lookup")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturnBadRequestForBinTooLarge() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("bin", 100000000)
                .auth().basic("testuser", "password")
                .when()
                .get("/bin-lookup")
                .then()
                .statusCode(BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldReturnUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("bin", 123456)
                .when()
                .get("/bin-lookup")
                .then()
                .statusCode(401);
    }
}
