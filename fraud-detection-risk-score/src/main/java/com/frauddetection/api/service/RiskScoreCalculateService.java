package com.frauddetection.api.service;

import com.frauddetection.api.report.RiskScoreReport;
import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.rule.RuleEvaluationException;
import com.frauddetection.api.rule.executor.RuleExecutor;
import com.frauddetection.api.transaction.TransactionParams;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Service for calculating the risk score of a transaction based on a set of rules.
 *
 * <p>
 * This service is responsible for executing the provided rules against the given transaction parameters
 * and generating a report that summarizes the results of the evaluation.
 * </p>
 */
@ApplicationScoped
public class RiskScoreCalculateService {


    /**
     * Calculates the risk score for a given set of rules and transaction parameters.
     *
     * <p>
     * This method executes the provided rules against the specified transaction parameters
     * and generates a {@link RiskScoreReport} that contains the results of the evaluation.
     * </p>
     *
     * @param rules             The set of rules to evaluate
     * @param transactionParams The transaction parameters to evaluate against the rules
     * @return A {@link RiskScoreReport} containing the results of the evaluation
     * @throws ExecutionException   If an error occurs during rule execution
     * @throws InterruptedException If the execution is interrupted
     */
    public RiskScoreReport calculate(Set<RiskRule> rules, TransactionParams transactionParams) throws ExecutionException, InterruptedException {
        RiskScoreReport report = new RiskScoreReport();
        if (rules == null || rules.isEmpty()) {
            return report;
        }

        report.setTotalRulesCount(rules.size());
        Map<RiskRule, RuleEvaluationException> executeRules = executeRules(rules, transactionParams);

        report.populate(executeRules);

        return report;
    }

    private Map<RiskRule, RuleEvaluationException> executeRules(Set<RiskRule> rules, TransactionParams transactionParams) throws ExecutionException, InterruptedException {
        RuleExecutor ruleExecutor = new RuleExecutor();
        return ruleExecutor.executeRules(rules, transactionParams);
    }
}
