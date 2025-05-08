package com.frauddetection.api.resource;

import com.frauddetection.api.dto.riskscore.RiskScoreRequest;
import com.frauddetection.api.dto.riskscore.RiskScoreResponse;
import com.frauddetection.api.service.riskscore.RiskScoreRequestHandler;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

@Path("/risk-score")
public class RiskScoreResource {

    private static final Logger LOGGER = Logger.getLogger(RiskScoreResource.class);

    @Inject
    Validator validator;
    @Inject
    RiskScoreRequestHandler riskScoreRequestHandler;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    @Operation(summary = "Calculate risk score based on transaction data")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Successful calculation of risk resulting in a report with total score and list of activated rules",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = RiskScoreResponse.class),
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "totalScore": 65,
                                                      "message": "Total number of evaluated rules: 6, fulfilled rules: 3, rules skipped: 1",
                                                      "fulfilledRules": [
                                                        { "ruleName": "High Transaction Amount", "ruleDescription": "Transaction amount exceeds the defined threshold: 1000", "score": 5 },
                                                        { "ruleName": "High Risk Country", "ruleDescription": "A transaction performed from a high-risk country", "score": 30 },
                                                        { "ruleName": "Anonymous Prepaid Card", "ruleDescription": "Transaction with anonymous prepaid card", "score": 40 }
                                                      ],
                                                      "rulesWithError": {
                                                        { "ruleName": "Out of Origin Country", "ruleDescription": "Transaction originating from a country different than the card's registered country", "score": 10 }: "Origin country code is not available for the transaction",
                                                      }
                                                    }"""
                                    )
                            }
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
    public Response getRiskScore(@Valid RiskScoreRequest request) {
        LOGGER.infof("New request with params %s", request);
        Response response = handleRequestAndGetResponse(request);
        LOGGER.infof("Returning response with status: %s and entity: %s", response.getStatus(), response.getEntity());
        return response;
    }

    private Response handleRequestAndGetResponse(RiskScoreRequest request) {
        List<String> violations = getViolations(request);
        if (!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(violations)
                    .build();
        }

        return riskScoreRequestHandler.handle(request).build();
    }

    private List<String> getViolations(RiskScoreRequest request) {
        Set<ConstraintViolation<RiskScoreRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            return violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();
        }

        return emptyList();
    }
}
