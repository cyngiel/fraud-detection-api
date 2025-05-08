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

class FlexCardRuleTest {

    @Mock
    private TransactionParams transactionParams;

    @Mock
    private BinDetails binDetails;

    private FlexCardRule rule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rule = new FlexCardRule(10, "FlexCard", "Check if card is flex card");
        when(transactionParams.getBinDetails()).thenReturn(Optional.of(binDetails));
    }

    @Test
    void shouldReturnTrueWhenIndicatorIsY() throws RuleEvaluationException {
        when(binDetails.getFlexCardIndicator()).thenReturn("Y");

        boolean result = rule.evaluate(transactionParams);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenIndicatorIsNotY() throws RuleEvaluationException {
        when(binDetails.getFlexCardIndicator()).thenReturn("N");

        boolean result = rule.evaluate(transactionParams);

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenIndicatorIsNull() {
        when(binDetails.getFlexCardIndicator()).thenReturn(null);

        assertThrows(RuleEvaluationException.class, () -> rule.evaluate(transactionParams));
    }

    @Test
    void shouldThrowExceptionWhenIndicatorIsEmpty() {
        when(binDetails.getFlexCardIndicator()).thenReturn("");

        assertThrows(RuleEvaluationException.class, () -> rule.evaluate(transactionParams));
    }
}