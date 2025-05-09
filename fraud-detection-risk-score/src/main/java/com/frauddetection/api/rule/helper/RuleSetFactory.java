package com.frauddetection.api.rule.helper;

import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.rule.definition.AnonymousPrepaidIndicatorRule;
import com.frauddetection.api.rule.definition.FlexCardRule;
import com.frauddetection.api.rule.definition.HighTransactionAmountRule;
import com.frauddetection.api.rule.definition.OutOfOriginCountryRule;
import com.frauddetection.api.rule.definition.TransactionFromHighRiskCountryRule;
import com.frauddetection.api.strategy.config.RiskScoreStrategyConfig;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;

@ApplicationScoped
public class RuleSetFactory {

    @Inject
    RiskScoreStrategyConfig riskScoreStrategyConfig;

    /**
     * Retrieves a set of rules based on the provided rule set name.
     *
     * @param ruleSetName the name of the rule set to retrieve
     * @return a set of {@link RiskRule} objects corresponding to the specified rule set
     * @throws IllegalArgumentException if the rule set name is unknown
     */
    @CacheResult(cacheName = "ruleSetCache")
    public Set<RiskRule> getRuleSet(String ruleSetName) {
        RiskScoreStrategyConfig.StrategyConfig ruleSet = riskScoreStrategyConfig.sets().get(ruleSetName);
        if (ruleSet == null) {
            throw new IllegalArgumentException("Unknown rule set: " + ruleSetName);
        }
        Set<RiskScoreStrategyConfig.RuleConfig> ruleConfigs = ruleSet.rules();
        if (ruleConfigs == null || ruleConfigs.isEmpty()) {
            return emptySet();
        }
        return getRules(ruleConfigs);
    }

    private Set<RiskRule> getRules(Set<RiskScoreStrategyConfig.RuleConfig> ruleConfigs) {
        Set<RiskRule> rules = new HashSet<>();

        for (RiskScoreStrategyConfig.RuleConfig ruleConfig : ruleConfigs) {
            switch (ruleConfig.type()) {
                case HIGH_RISK_COUNTRY -> rules.add(new TransactionFromHighRiskCountryRule(ruleConfig.score(), ruleConfig.name(), ruleConfig.description()));
                case OUT_OF_ORIGIN_COUNTRY -> rules.add(new OutOfOriginCountryRule(ruleConfig.score(), ruleConfig.name(), ruleConfig.description()));
                case HIGH_TRANSACTION_AMOUNT -> rules.add(new HighTransactionAmountRule(ruleConfig.score(), ruleConfig.name(), ruleConfig.description(), ruleConfig.threshold().orElseThrow()));
                case ANONYMOUS_PREPAID_INDICATOR -> rules.add(new AnonymousPrepaidIndicatorRule(ruleConfig.score(), ruleConfig.name(), ruleConfig.description()));
                case FLEX_CARD_INDICATOR -> rules.add(new FlexCardRule(ruleConfig.score(), ruleConfig.name(), ruleConfig.description()));
            }
        }

        return rules;
    }
}
