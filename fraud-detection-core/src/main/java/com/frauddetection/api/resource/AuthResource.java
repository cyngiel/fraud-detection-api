package com.frauddetection.api.resource;

import com.frauddetection.api.dto.user.AuthenticatedUser;
import com.frauddetection.api.dto.user.UserCredentials;
import com.frauddetection.api.service.security.AuthenticationService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/auth")
public class AuthResource {

    @Inject
    AuthenticationService authenticationService;


    @POST
    @Path("/login")
    @PermitAll
    public Response login(UserCredentials credentials) {
        System.out.println("Request: " + credentials.username() + " " + credentials.password());
        Optional<AuthenticatedUser> authenticatedUser = authenticationService.authenticateUser(credentials);
        if (authenticatedUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("token", authenticatedUser.get().token());
            return Response.ok(response).build();
        }

        return Response.status(UNAUTHORIZED).build();
    }
}
