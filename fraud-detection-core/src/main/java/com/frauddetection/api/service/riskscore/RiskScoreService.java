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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.Optional;

@ApplicationScoped
public class RiskScoreService {

    private static final Logger LOGGER = Logger.getLogger(RiskScoreService.class);

    @Inject
    RiskScoreStrategyManager riskScoreStrategyManager;

    @Inject
    BinLookupService binLookupService;

    @Inject
    TransactionRepository transactionRepository;

    @Inject
    RequestContext requestContext;

    /**
     * Executes the risk score calculation for a given transaction.
     *
     * <p>
     * This method determines the appropriate risk score strategy based on the availability
     * of BIN details for the transaction. It retrieves BIN details, selects the corresponding
     * strategy, executes the strategy, and generates a risk score report.
     * </br>The transaction information and the generated report are persisted.
     * </p>
     *
     * @param transactionParams transaction parameters
     * @return {@link RiskScoreReport} with the result of transaction execution
     * @throws StrategyExecutionException if an error occurs during the execution of the risk score strategy
     * @throws IOException if an error occurs while retrieving BIN details
     */
    public RiskScoreReport executeRiskScoreCalculation(TransactionParams transactionParams) throws StrategyExecutionException, IOException {
        LOGGER.infof("Start evaluating risk score for transaction: %s", transactionParams);

        ExecutableStrategy strategy;

        Optional<BinDetails> binDetails = binLookupService.getBinDetails(transactionParams.getBin());
        if (binDetails.isPresent()) {
            transactionParams.setBinDetails(binDetails.get());
            strategy = riskScoreStrategyManager.getDefaultStrategy("DEFAULT");
        } else {
            strategy = riskScoreStrategyManager.getDefaultStrategy("WITHOUT_BIN_DETAILS");
        }

        LOGGER.infof("Executing strategy: %s", strategy.getClass().getSimpleName());

        RiskScoreReport report = strategy.execute(transactionParams);

        LOGGER.infof("Risk score strategy completed with total score: %d", report.getTotalRiskScore());
        LOGGER.debugf("Risk score full report: %s", report);

        persistTransactionInfo(transactionParams, report);

        return report;
    }

    @Transactional
    public void persistTransactionInfo(TransactionParams transactionParams, RiskScoreReport report) {
        transactionRepository.persist(new ProcessedTransaction(
                transactionParams.getBin(),
                transactionParams.getCountryCode(),
                transactionParams.getAmount(),
                report.getTotalRiskScore(),
                requestContext.getRequestId()));
    }
}
