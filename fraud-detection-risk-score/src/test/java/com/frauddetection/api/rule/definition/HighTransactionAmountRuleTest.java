package com.frauddetection.api.rule.definition;

import com.frauddetection.api.transaction.TransactionParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HighTransactionAmountRuleTest {

    @Mock
    private TransactionParams transactionParams;

    private HighTransactionAmountRule rule;
    private final double threshold = 1000.0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rule = new HighTransactionAmountRule(10, "HighTransactionAmount", "Transaction amount exceeds ", threshold);
    }

    @Test
    void shouldReturnTrueWhenAmountIsGreaterThanThreshold() {
        when(transactionParams.getAmount()).thenReturn(1500.0);

        boolean result = rule.evaluate(transactionParams);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenAmountIsEqualToThreshold() {
        when(transactionParams.getAmount()).thenReturn(1000.0);

        boolean result = rule.evaluate(transactionParams);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenAmountIsLessThanThreshold() {
        when(transactionParams.getAmount()).thenReturn(500.0);

        boolean result = rule.evaluate(transactionParams);

        assertFalse(result);
    }
}