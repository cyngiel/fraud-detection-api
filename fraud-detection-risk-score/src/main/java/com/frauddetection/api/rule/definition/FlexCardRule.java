package com.frauddetection.api.rule.definition;

import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.rule.RuleEvaluationException;
import com.frauddetection.api.transaction.TransactionParams;
import lombok.ToString;

@ToString(callSuper = true)
public class FlexCardRule extends AbstractRule{

    public FlexCardRule(int score, String ruleName, String ruleDescription) {
        super(score, ruleName, ruleDescription);
    }

    @Override
    public boolean evaluate(TransactionParams transactionParams) throws RuleEvaluationException {
        String flexCardIndicator = getBinDetails(transactionParams).getFlexCardIndicator();
        if (flexCardIndicator == null || flexCardIndicator.isEmpty()) {
            throw new RuleEvaluationException("Flex indicator is not available for the transaction");
        }
        return flexCardIndicator.equals("Y");
    }
}
