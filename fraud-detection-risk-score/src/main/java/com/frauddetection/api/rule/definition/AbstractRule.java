package com.frauddetection.api.rule.definition;

import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.report.ReportEntry;
import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.rule.RuleEvaluationException;
import com.frauddetection.api.transaction.TransactionParams;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public abstract class AbstractRule implements RiskRule, ReportEntry {

    int score;
    String ruleName;
    String ruleDescription;

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getRuleName() {
        return ruleName;
    }

    @Override
    public String getRuleDescription() {
        return ruleDescription;
    }

    BinDetails getBinDetails(TransactionParams transactionParams) throws RuleEvaluationException {
        return transactionParams.getBinDetails()
                .orElseThrow(() -> new RuleEvaluationException("Bin details are not available for the transaction"));
    }
}
