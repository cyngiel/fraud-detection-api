package com.frauddetection.api.rule.definition;

import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.rule.RuleEvaluationException;
import com.frauddetection.api.transaction.TransactionParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AnonymousPrepaidIndicatorRuleTest {

    @Mock
    private TransactionParams transactionParams;

    @Mock
    private BinDetails binDetails;

    private AnonymousPrepaidIndicatorRule rule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rule = new AnonymousPrepaidIndicatorRule(10, "AnonymousPrepaid", "Check if card is anonymous prepaid");
        when(transactionParams.getBinDetails()).thenReturn(Optional.of(binDetails));
    }

    @Test
    void shouldReturnTrueWhenIndicatorIsA() throws RuleEvaluationException {
        when(binDetails.getAnonymousPrepaidIndicator()).thenReturn("A");

        boolean result = rule.evaluate(transactionParams);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenIndicatorIsNotA() throws RuleEvaluationException {
        when(binDetails.getAnonymousPrepaidIndicator()).thenReturn("N");

        boolean result = rule.evaluate(transactionParams);

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenIndicatorIsNull() {
        when(binDetails.getAnonymousPrepaidIndicator()).thenReturn(null);

        assertThrows(RuleEvaluationException.class, () -> rule.evaluate(transactionParams));
    }

    @Test
    void shouldThrowExceptionWhenIndicatorIsEmpty() {
        when(binDetails.getAnonymousPrepaidIndicator()).thenReturn("");

        assertThrows(RuleEvaluationException.class, () -> rule.evaluate(transactionParams));
    }
}
