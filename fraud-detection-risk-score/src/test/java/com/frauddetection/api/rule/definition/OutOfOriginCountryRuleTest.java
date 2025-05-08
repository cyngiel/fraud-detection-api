package com.frauddetection.api.rule.definition;

import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.rule.RuleEvaluationException;
import com.frauddetection.api.transaction.TransactionParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class OutOfOriginCountryRuleTest {

    @Mock
    private TransactionParams transactionParams;

    @Mock
    private BinDetails binDetails;

    @Mock
    private BinDetails.Country country;

    private OutOfOriginCountryRule rule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rule = new OutOfOriginCountryRule(10, "OutOfOriginCountry", "Transaction performed outside card's origin country");
        when(transactionParams.getBinDetails()).thenReturn(Optional.of(binDetails));
        when(binDetails.getCountry()).thenReturn(country);
    }

    @Test
    void shouldReturnTrueWhenCountryCodesDoNotMatch() throws RuleEvaluationException {
        when(country.getAlpha3()).thenReturn("USA");
        when(transactionParams.getCountryCode()).thenReturn("CAN");

        boolean result = rule.evaluate(transactionParams);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenCountryCodesMatch() throws RuleEvaluationException {
        when(country.getAlpha3()).thenReturn("USA");
        when(transactionParams.getCountryCode()).thenReturn("USA");

        boolean result = rule.evaluate(transactionParams);

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenOriginCountryCodeIsNull() {
        when(country.getAlpha3()).thenReturn(null);

        assertThrows(RuleEvaluationException.class, () -> rule.evaluate(transactionParams));
    }

    @Test
    void shouldThrowExceptionWhenOriginCountryCodeIsEmpty() {
        when(country.getAlpha3()).thenReturn("");

        assertThrows(RuleEvaluationException.class, () -> rule.evaluate(transactionParams));
    }
}
