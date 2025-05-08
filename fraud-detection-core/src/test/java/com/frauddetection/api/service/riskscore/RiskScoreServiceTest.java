package com.frauddetection.api.service.riskscore;

import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.filter.RequestContext;
import com.frauddetection.api.report.RiskScoreReport;
import com.frauddetection.api.repository.TransactionRepository;
import com.frauddetection.api.repository.entity.ProcessedTransaction;
import com.frauddetection.api.service.binlookup.BinLookupService;
import com.frauddetection.api.strategy.ExecutableStrategy;
import com.frauddetection.api.strategy.RiskScoreStrategyManager;
import com.frauddetection.api.strategy.StrategyExecutionException;
import com.frauddetection.api.transaction.TransactionParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiskScoreServiceTest {

    @Mock
    private RiskScoreStrategyManager riskScoreStrategyManager;

    @Mock
    private BinLookupService binLookupService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RequestContext requestContext;

    @Mock
    private ExecutableStrategy defaultStrategy;

    @Mock
    private ExecutableStrategy withoutBinStrategy;

    @Mock
    private TransactionParams transactionParams;

    @Mock
    private BinDetails binDetails;

    @Mock
    private RiskScoreReport riskScoreReport;

    @InjectMocks
    private RiskScoreService riskScoreService;

    private static final int BIN = 12345678;
    private static final String COUNTRY_CODE = "USA";
    private static final double AMOUNT = 1000.0;
    private static final int RISK_SCORE = 75;
    private static final String REQUEST_ID = "test-request-id";

    @BeforeEach
    void setUp() {
        when(transactionParams.getBin()).thenReturn(BIN);
        when(transactionParams.getCountryCode()).thenReturn(COUNTRY_CODE);
        when(transactionParams.getAmount()).thenReturn(AMOUNT);
        when(requestContext.getRequestId()).thenReturn(REQUEST_ID);
        when(riskScoreReport.getTotalRiskScore()).thenReturn(RISK_SCORE);
    }

    @Test
    void shouldUseDefaultStrategyWhenBinDetailsAreAvailable() throws StrategyExecutionException, IOException {
        when(binLookupService.getBinDetails(BIN)).thenReturn(Optional.of(binDetails));
        when(riskScoreStrategyManager.getDefaultStrategy("DEFAULT")).thenReturn(defaultStrategy);
        when(defaultStrategy.execute(transactionParams)).thenReturn(riskScoreReport);

        RiskScoreReport result = riskScoreService.executeRiskScoreCalculation(transactionParams);

        assertEquals(riskScoreReport, result);
        verify(transactionParams).setBinDetails(binDetails);
        verify(defaultStrategy).execute(transactionParams);
        verify(transactionRepository).persist(any(ProcessedTransaction.class));
    }

    @Test
    void shouldUseWithoutBinStrategyWhenBinDetailsAreNotAvailable() throws StrategyExecutionException, IOException {
        when(binLookupService.getBinDetails(BIN)).thenReturn(Optional.empty());
        when(riskScoreStrategyManager.getDefaultStrategy("WITHOUT_BIN_DETAILS")).thenReturn(withoutBinStrategy);
        when(withoutBinStrategy.execute(transactionParams)).thenReturn(riskScoreReport);

        RiskScoreReport result = riskScoreService.executeRiskScoreCalculation(transactionParams);

        assertEquals(riskScoreReport, result);
        verify(transactionParams, never()).setBinDetails(any());
        verify(withoutBinStrategy).execute(transactionParams);
        verify(transactionRepository).persist(any(ProcessedTransaction.class));
    }

    @Test
    void shouldPersistTransactionInformation() throws StrategyExecutionException, IOException {
        when(binLookupService.getBinDetails(BIN)).thenReturn(Optional.of(binDetails));
        when(riskScoreStrategyManager.getDefaultStrategy("DEFAULT")).thenReturn(defaultStrategy);
        when(defaultStrategy.execute(transactionParams)).thenReturn(riskScoreReport);

        ArgumentCaptor<ProcessedTransaction> transactionCaptor = ArgumentCaptor.forClass(ProcessedTransaction.class);

        riskScoreService.executeRiskScoreCalculation(transactionParams);

        verify(transactionRepository).persist(transactionCaptor.capture());
        ProcessedTransaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(BIN, capturedTransaction.getBinNumber());
        assertEquals(COUNTRY_CODE, capturedTransaction.getCountryCode());
        assertEquals(AMOUNT, capturedTransaction.getAmount());
        assertEquals(RISK_SCORE, capturedTransaction.getRiskScore());
        assertEquals(REQUEST_ID, capturedTransaction.getXRequestID());
    }
}