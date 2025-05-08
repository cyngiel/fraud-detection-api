package com.frauddetection.api.strategy;

import com.frauddetection.api.rule.helper.RuleSetFactory;
import com.frauddetection.api.service.RiskScoreCalculateService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RiskScoreStrategyManager {

    @Inject
    RuleSetFactory ruleSetFactory;

    @Inject
    RiskScoreCalculateService calculateService;

    public ExecutableStrategy getDefaultStrategy(String ruleSetName) {
        return new DefaultRiskScoreStrategy(ruleSetName, ruleSetFactory, calculateService);
    }
}
