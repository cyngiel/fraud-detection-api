package com.frauddetection.api.strategy;

import com.frauddetection.api.report.RiskScoreReport;
import com.frauddetection.api.rule.helper.RuleSetFactory;
import com.frauddetection.api.service.RiskScoreCalculateService;
import com.frauddetection.api.transaction.TransactionParams;

 /**
  * Simple implementation of the risk score strategy.
  *
  * <p>
  * This strategy represents the default behavior for calculating the risk score
  * of a transaction. It executes a single set of rules defined by the `ruleSetName` parameter.
  * It determines which specific set of rules will be retrieved and applied
  * during the risk score calculation process.
  * </p>
  *
  * @see AbstractRiskScoreStrategy
  * @see ExecutableStrategy
  */
public class DefaultRiskScoreStrategy extends AbstractRiskScoreStrategy {

    private final String ruleSetName;

    public DefaultRiskScoreStrategy(String ruleSetName, RuleSetFactory ruleSetFactory, RiskScoreCalculateService calculateService) {
        super(ruleSetFactory, calculateService);
        this.ruleSetName = ruleSetName;
    }

    @Override
    public RiskScoreReport execute(TransactionParams transactionParams) throws StrategyExecutionException {
        RiskScoreReport report;

        try {
            report = calculateService.calculate(getRules(ruleSetName), transactionParams);
        } catch (Exception e) {
            LOGGER.error(e.getStackTrace());
            throw new StrategyExecutionException(e);
        }

        return report;
    }
}
