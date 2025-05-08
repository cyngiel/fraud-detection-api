package com.frauddetection.api.strategy;

import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.rule.helper.RuleSetFactory;
import com.frauddetection.api.service.RiskScoreCalculateService;
import org.jboss.logging.Logger;

import java.util.Set;

/**
 * Abstract base class for risk score strategies.
 *
 * <p>
 * This class provides common functionality for all risk score strategies,
 * such as retrieving rules from the `RuleSetFactory` and initializing shared services.
 * </p>
 *
 * @see ExecutableStrategy
 */
public abstract class AbstractRiskScoreStrategy implements ExecutableStrategy {

    Logger LOGGER = Logger.getLogger(AbstractRiskScoreStrategy.class);
    RuleSetFactory ruleSetFactory;
    RiskScoreCalculateService calculateService;

   public AbstractRiskScoreStrategy(RuleSetFactory ruleSetFactory, RiskScoreCalculateService calculateService) {
           this.ruleSetFactory = ruleSetFactory;
           this.calculateService = calculateService;
       }

    Set<RiskRule> getRules(String strategyName) {
        return ruleSetFactory.getRuleSet(strategyName);
    }
}
