package com.frauddetection.api.rule;

import com.frauddetection.api.transaction.TransactionParams;

public interface RiskRule {
    /**
     * Evaluates the risk rule based on the provided transaction parameters.
     *
     * @param transactionParams The transaction parameters to evaluate.
     * @return true if the rule is satisfied, false otherwise.
     * @throws RuleEvaluationException If an error occurs during rule evaluation.
     */
    boolean evaluate(TransactionParams transactionParams) throws RuleEvaluationException;
}
