package com.frauddetection.api.rule.helper;

import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.strategy.config.RiskScoreStrategyConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class RuleSetFactoryTest {

    @Mock
    private RiskScoreStrategyConfig riskScoreStrategyConfig;

    @InjectMocks
    private RuleSetFactory ruleSetFactory;

    @Mock
    private RiskScoreStrategyConfig.StrategyConfig strategyConfig;

    private Map<String, RiskScoreStrategyConfig.StrategyConfig> strategiesMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        strategiesMap = new HashMap<>();

        when(riskScoreStrategyConfig.sets()).thenReturn(strategiesMap);
    }

    @Test
    void shouldThrowExceptionWhenRuleSetNameIsUnknown() {
        String unknownRuleSetName = "unknown";

        assertThrows(IllegalArgumentException.class, () -> ruleSetFactory.getRuleSet(unknownRuleSetName));
    }

    @Test
    void shouldReturnEmptySetWhenRuleConfigsAreEmpty() {
        String ruleSetName = "emptyRuleSet";
        strategiesMap.put(ruleSetName, strategyConfig);
        when(strategyConfig.rules()).thenReturn(Collections.emptySet());

        Set<RiskRule> result = ruleSetFactory.getRuleSet(ruleSetName);

        assertTrue(result.isEmpty());
    }
}
