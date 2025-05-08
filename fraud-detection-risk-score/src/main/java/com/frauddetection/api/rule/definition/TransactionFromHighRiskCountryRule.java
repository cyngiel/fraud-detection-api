package com.frauddetection.api.rule.definition;


import com.frauddetection.api.rule.helper.RuleHelper;
import com.frauddetection.api.transaction.TransactionParams;
import lombok.ToString;

@ToString(callSuper = true)
public class TransactionFromHighRiskCountryRule extends AbstractRule {


    public TransactionFromHighRiskCountryRule(int score, String ruleName, String ruleDescription) {
        super(score, ruleName, ruleDescription);
    }

    @Override
    public boolean evaluate(TransactionParams transactionParams) {
        return isHighRiskCountry(transactionParams.getCountryCode()) ;
    }

    private boolean isHighRiskCountry(String countryCode) {
        return RuleHelper.getHighRiskCountries().stream()
                .anyMatch(country -> country.getCode().equals(countryCode));
    }
}
