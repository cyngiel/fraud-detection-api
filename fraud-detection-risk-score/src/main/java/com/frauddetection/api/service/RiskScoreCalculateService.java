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
