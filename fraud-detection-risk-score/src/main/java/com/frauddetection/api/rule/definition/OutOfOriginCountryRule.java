package com.frauddetection.api.rule.definition;

import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.rule.RuleEvaluationException;
import com.frauddetection.api.transaction.TransactionParams;
import lombok.ToString;

@ToString(callSuper = true)
public class OutOfOriginCountryRule extends AbstractRule {


    public OutOfOriginCountryRule(int score, String ruleName, String ruleDescription) {
        super(score, ruleName, ruleDescription);
    }

    @Override
    public boolean evaluate(TransactionParams transactionParams) throws RuleEvaluationException {
        String originCountryCode = getBinDetails(transactionParams).getCountry().getAlpha3();
        if (originCountryCode == null || originCountryCode.isEmpty()) {
            throw new RuleEvaluationException("Origin country code is not available for the transaction");
        }
        return !originCountryCode.equals(transactionParams.getCountryCode());
    }
}
