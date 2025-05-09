package com.frauddetection.api.service.riskscore;

import com.frauddetection.api.dto.riskscore.RiskRuleDetails;
import com.frauddetection.api.dto.riskscore.RiskScoreRequest;
import com.frauddetection.api.dto.riskscore.RiskScoreResponse;
import com.frauddetection.api.report.RiskScoreReport;
import com.frauddetection.api.transaction.TransactionParams;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

/**
 * Handles the risk score calculation request.
 *
 * <p>
 * This class is responsible for processing the risk score request, executing the risk score calculation,
 * and building the response based on the result of the calculation.
 * </p>
 */
@ApplicationScoped
public class RiskScoreRequestHandler {

    private static final Logger LOGGER = Logger.getLogger(RiskScoreRequestHandler.class);

    @Inject
    RiskScoreService riskScoreService;

    /**
     * Handles the risk score request.
     *
     * @param request the risk score request
     * @return the response builder with the result of the calculation
     */
    public Response.ResponseBuilder handle(RiskScoreRequest request) {
        Response.ResponseBuilder response;

        try {
            RiskScoreReport report = riskScoreService.executeRiskScoreCalculation(getTransactionParams(request));
            response = Response.ok(buildRiskScoreResponse(report));
        } catch (Exception e) {
            LOGGER.error(e);
            response = Response.serverError().entity("An internal server error occurred. Please contact support with Request-ID");
        }

        return response;
    }

    private TransactionParams getTransactionParams(RiskScoreRequest request) {
        return TransactionParams.builder()
                .bin(request.getBin())
                .countryCode(request.getCountryCode())
                .amount(request.getTransactionAmount())
                .build();
    }

    private RiskScoreResponse buildRiskScoreResponse(RiskScoreReport report) {
        List<RiskRuleDetails> fulfilledRules = report.getFulfilledRules().stream()
                .map(entry -> new RiskRuleDetails(entry.getRuleName(), entry.getRuleDescription(), entry.getScore()))
                .toList();

        Map<RiskRuleDetails, String> rulesWithError = report.getRulesWithError().entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        entry -> new RiskRuleDetails(entry.getKey().getRuleName(), entry.getKey().getRuleDescription(), entry.getKey().getScore()),
                        Map.Entry::getValue
                ));

        return new RiskScoreResponse(report.getFlag().toString(), report.getTotalRiskScore(), report.getTotalRulesCount(), fulfilledRules, rulesWithError);
    }
}
