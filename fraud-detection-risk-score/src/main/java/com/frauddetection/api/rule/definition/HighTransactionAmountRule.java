package com.frauddetection.api.rule.definition;

import com.frauddetection.api.transaction.TransactionParams;
import lombok.ToString;


@ToString(callSuper = true)
public class HighTransactionAmountRule extends AbstractRule {

    private final double threshold;

    public HighTransactionAmountRule(int score, String ruleName, String ruleDescription, double threshold) {
        super(score, ruleName, ruleDescription + threshold);
        this.threshold = threshold;
    }

    @Override
    public boolean evaluate(TransactionParams transactionParams) {
        return transactionParams.getAmount() > threshold;
    }
}
