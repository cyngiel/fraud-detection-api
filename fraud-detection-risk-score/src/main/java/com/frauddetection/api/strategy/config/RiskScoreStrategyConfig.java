package com.frauddetection.api.strategy.config;

import com.frauddetection.api.rule.RuleType;
import com.frauddetection.api.rule.helper.RuleTypeConverter;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithName;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ConfigMapping(prefix = "risk-score")
public interface RiskScoreStrategyConfig {
    Map<String, StrategyConfig> sets();

    interface StrategyConfig {
        Set<RuleConfig> rules();
    }

    interface RuleConfig {
        @WithName("type")
        @WithConverter(RuleTypeConverter.class)
        RuleType type();

        @WithName("name")
        String name();

        @WithName("score")
        int score();

        @WithName("description")
        String description();

        @WithName("threshold")
        Optional<Double> threshold();
    }
}