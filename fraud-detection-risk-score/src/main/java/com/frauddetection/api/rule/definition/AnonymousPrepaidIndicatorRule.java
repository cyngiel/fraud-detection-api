package com.frauddetection.api.rule.definition;


import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.rule.RuleEvaluationException;
import com.frauddetection.api.transaction.TransactionParams;
import lombok.ToString;

@ToString(callSuper = true)
public class AnonymousPrepaidIndicatorRule extends AbstractRule{

    public AnonymousPrepaidIndicatorRule(int score, String ruleName, String ruleDescription) {
        super(score, ruleName, ruleDescription);
    }

    @Override
    public boolean evaluate(TransactionParams transactionParams) throws RuleEvaluationException {
        String anonymousPrepaidIndicator = getBinDetails(transactionParams).getAnonymousPrepaidIndicator();
        if (anonymousPrepaidIndicator == null || anonymousPrepaidIndicator.isEmpty()) {
            throw new RuleEvaluationException("Anonymous prepaid indicator is not available for the transaction");
        }

        return anonymousPrepaidIndicator.equals("A");
    }
}
