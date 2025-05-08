package com.frauddetection.api.rule;

import lombok.Getter;

@Getter
public enum RuleType {
    HIGH_RISK_COUNTRY("high-risk-country"),
    OUT_OF_ORIGIN_COUNTRY("out-of-origin-country"),
    HIGH_TRANSACTION_AMOUNT("high-transaction-amount"),
    ANONYMOUS_PREPAID_INDICATOR("anonymous-prepaid-indicator"),
    FLEX_CARD_INDICATOR("flex-card-indicator");

    private final String value;

    RuleType(String value) {
        this.value = value;
    }
}