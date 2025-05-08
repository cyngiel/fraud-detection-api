package com.frauddetection.api.dto.riskscore;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

@Getter
public class RiskScoreResponse {
    private final int totalScore;
    private final String flag;
    private final String message;
    private final Collection<RiskRuleDetails> fulfilledRules;
    private final Map<RiskRuleDetails, String> rulesWithError;

    public RiskScoreResponse(String flag, int totalScore, int totalRulesCount, Collection<RiskRuleDetails> fulfilledRules, Map<RiskRuleDetails, String> rulesWithError) {
        this.flag = flag;
        String message = buildMessage(totalRulesCount, fulfilledRules.size(), rulesWithError.size());
        this.totalScore = totalScore;
        this.message = message;
        this.fulfilledRules = fulfilledRules;
        this.rulesWithError = rulesWithError;
    }

    private static @NotNull String buildMessage(int totalRulesCount, int fulfilledRulesCount, int rulesWithErrorCount) {
        return new StringBuilder()
                .append("Total number of evaluated rules: ")
                .append(totalRulesCount)
                .append(", fulfilled rules: ")
                .append(fulfilledRulesCount)
                .append(", rules with error: ")
                .append(rulesWithErrorCount)
                .toString();
    }

}
