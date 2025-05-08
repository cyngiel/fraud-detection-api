package com.frauddetection.api.rule.executor;

import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.rule.RuleEvaluationException;
import com.frauddetection.api.transaction.TransactionParams;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.synchronizedMap;

/**
 * RuleExecutor is responsible for executing a set of rules against transaction parameters.
 * It uses a thread pool to execute the rules concurrently.
 */
public class RuleExecutor {

    private final Logger LOGGER = Logger.getLogger(RuleExecutor.class);
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public Map<RiskRule, RuleEvaluationException> executeRules(Set<RiskRule> rules, TransactionParams transactionParams) throws ExecutionException, InterruptedException {
        return execute(rules, transactionParams);
    }

    private Map<RiskRule, RuleEvaluationException> execute(Set<RiskRule> rules, TransactionParams transactionParams) throws InterruptedException, ExecutionException {
        Map<RiskRule, RuleEvaluationException> fulfilledRules = synchronizedMap(new HashMap<>());

        //        for (RiskRule rule : rules) {
        //            executor.submit(() -> {
        //                handleCalculateRiskScore(rule, transactionParams, fulfilledRules);
        //            });
        //        }

        rules.forEach(rule -> {
            handleCalculateRiskScore(rule, transactionParams, fulfilledRules);
        });

        executor.shutdown();
        boolean terminated = executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        LOGGER.debugf("Finished rule execution | {} ", terminated ? "executor terminated" : "timeout elapsed before terminated");

        return fulfilledRules;
    }

    private void handleCalculateRiskScore(RiskRule rule, TransactionParams transactionParams, Map<RiskRule, RuleEvaluationException> fulfilledRules) {
        try {
            boolean ruleApplied = rule.evaluate(transactionParams);
            if (ruleApplied) {
                fulfilledRules.put(rule, null);
            }
        } catch (RuleEvaluationException e) {
            LOGGER.debugf("Error evaluating rule: %s", e.getMessage());
            fulfilledRules.put(rule, e);
        } catch (Exception e) {
            LOGGER.errorf("Unexpected error evaluating rule: %s", e.getMessage());
            fulfilledRules.put(rule, new RuleEvaluationException("Unexpected error evaluating rule"));
        }
    }
}
