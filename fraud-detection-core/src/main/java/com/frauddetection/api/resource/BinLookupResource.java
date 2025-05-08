package com.frauddetection.api.resource;


import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.service.binlookup.BinLookupService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.io.IOException;
import java.util.Optional;


@Path("/bin-lookup")
public class BinLookupResource {

    @Inject
    BinLookupService binLookupService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    @Operation(summary = "Fetch single BIN details")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = BinDetails.class)
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN
                    )
            )
    })
    public Response getBinDetails(@Min(value = 100000, message = "BIN must have at least 6 digits")
                                  @Max(value = 99999999, message = "BIN must have at most 8 digits")
                                  @Parameter(description = "Single 6-8 digits BIN", example = "99630215")
                                  @QueryParam("bin") int binNumber) {

        return handleRequestAndGetResponse(binNumber).build();
    }

    private Response.ResponseBuilder handleRequestAndGetResponse(int binNumber) {
        Response.ResponseBuilder response;
        try {
            Optional<BinDetails> binDetails = binLookupService.getBinDetails(binNumber);
            if (binDetails.isPresent()) {
                response = Response.ok(binDetails.get());
            } else {
                response = Response.status(Response.Status.NOT_FOUND).entity("No data for BIN: " + binNumber);
            }
        } catch (IOException e) {
            response = Response.serverError().entity("An internal server error occurred. Please contact support with Request-ID");
        }
        return response;
    }
}

