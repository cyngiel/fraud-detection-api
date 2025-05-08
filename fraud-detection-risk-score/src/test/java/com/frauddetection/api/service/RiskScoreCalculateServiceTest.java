package com.frauddetection.api.service;

import com.frauddetection.api.report.ReportEntry;
import com.frauddetection.api.report.RiskScoreReport;
import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.rule.executor.RuleExecutor;
import com.frauddetection.api.transaction.TransactionParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@ExtendWith(MockitoExtension.class)
class RiskScoreCalculateServiceTest {

    @InjectMocks
    private RiskScoreCalculateService service;

    private TransactionParams transactionParams;

    @BeforeEach
    void setUp() {
        transactionParams = mock(TransactionParams.class);
    }

    @Test
    void shouldReturnEmptyReportWhenRulesAreNull() throws ExecutionException, InterruptedException {
        RiskScoreReport report = service.calculate(null, transactionParams);

        assertNotNull(report);
        assertNull(report.getFulfilledRules());
    }

    @Test
    void shouldReturnEmptyReportWhenRulesAreEmpty() throws ExecutionException, InterruptedException {
        RiskScoreReport report = service.calculate(Collections.emptySet(), transactionParams);

        assertNotNull(report);
        assertNull(report.getFulfilledRules());
    }

    @Test
    void shouldCountExecutedRules() throws ExecutionException, InterruptedException {
        RiskRule reportRule1 = mock(RiskRule.class, withSettings().extraInterfaces(ReportEntry.class));
        RiskRule reportRule2 = mock(RiskRule.class, withSettings().extraInterfaces(ReportEntry.class));
        Set<RiskRule> rules = Set.of(reportRule1, reportRule2);

        try (MockedConstruction<RuleExecutor> mockedConstruction = mockConstruction(RuleExecutor.class,
                (mock, context) -> {
                    when(mock.executeRules(any(), any())).thenReturn(new HashMap<>());
                })) {

            RiskScoreReport report = service.calculate(rules, transactionParams);

            assertNotNull(report);
            assertEquals(2, report.getTotalRulesCount());
            assertEquals(0, report.getTotalRiskScore());
        }
    }

    @Test
    void shouldHandleExceptionFromRuleExecutor() {
        Set<RiskRule> rules = new HashSet<>();
        rules.add(mock(RiskRule.class));

        RuntimeException testException = new RuntimeException("Test exception");
        ExecutionException executionException = new ExecutionException(testException);

        try (MockedConstruction<RuleExecutor> mockedConstruction = mockConstruction(RuleExecutor.class,
                (mock, context) -> {
                    when(mock.executeRules(any(), any())).thenThrow(executionException);
                })) {

            ExecutionException thrown = assertThrows(ExecutionException.class,
                    () -> service.calculate(rules, transactionParams));
            assertEquals(testException, thrown.getCause());
        }
    }
}