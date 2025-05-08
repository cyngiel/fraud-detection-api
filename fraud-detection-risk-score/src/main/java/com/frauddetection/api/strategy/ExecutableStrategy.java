package com.frauddetection.api.strategy;

import com.frauddetection.api.report.RiskScoreReport;
import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.transaction.TransactionParams;

/**
 * Defines a strategy for calculating risk scores for transactions.
 * <p>
 * This interface represents the Strategy pattern for risk score calculation process.
 * Different implementations can provide various algorithms for determining the risk level of transactions based on different rule sets.
 * <p>
 * Implementations of this interface are managed by {@link RiskScoreStrategyManager}
 * and are used to calculate risk scores for transaction parameters.
 * <p>
 * A typical implementation will use a set of {@link RiskRule}
 * to evaluate different aspects of a transaction and compile the results into a {@link RiskScoreReport}.
 *
 * @see DefaultRiskScoreStrategy
 * @see com.frauddetection.api.report.RiskScoreReport
 * @see com.frauddetection.api.transaction.TransactionParams
 */
public interface ExecutableStrategy {

    /**
     * Executes the risk scoring strategy on the provided transaction parameters.
     *
     * @param transactionParams The transaction parameters to evaluate for risk
     * @return A report containing the calculated risk scores and details
     * @throws StrategyExecutionException If an error occurs during strategy execution
     */
    RiskScoreReport execute(TransactionParams transactionParams) throws StrategyExecutionException;
}
