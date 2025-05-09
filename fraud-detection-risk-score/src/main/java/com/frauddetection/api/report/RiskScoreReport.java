package com.frauddetection.api.report;

import com.frauddetection.api.rule.RiskRule;
import com.frauddetection.api.rule.RuleEvaluationException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.frauddetection.api.report.RiskFlag.HIGH_RISK;
import static com.frauddetection.api.report.RiskFlag.SAFE;
import static com.frauddetection.api.report.RiskFlag.WARNING;

/**
 * RiskScoreReport is a class that represents the report generated after evaluating a set of risk rules.
 * It contains information about the total number of rules, the total risk score, the risk flag,
 * and any rules that encountered errors during evaluation.
 */
@Getter
@ToString
public class RiskScoreReport {

    @Setter
    private int totalRulesCount = 0;
    private int totalRiskScore = 0;
    private RiskFlag flag = SAFE;
    private Map<ReportEntry, String> rulesWithError;
    private Set<ReportEntry> fulfilledRules;

    public void populate(Map<RiskRule, RuleEvaluationException> executeRules) {
        addFulfilledRules(executeRules.entrySet().stream()
                .filter(entry -> entry.getValue() == null)
                .map(Map.Entry::getKey)
                .filter(rule -> rule instanceof ReportEntry)
                .map(rule -> (ReportEntry) rule)
                .collect(Collectors.toSet()));

        addRulesWithError(executeRules.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> entry.getKey() instanceof ReportEntry)
                .collect(Collectors.toMap(
                        entry -> (ReportEntry) entry.getKey(),
                        entry -> entry.getValue().getMessage()
                )));
    }

    private void addFulfilledRules(Set<ReportEntry> reportEntries) {
        if (fulfilledRules == null) {
            fulfilledRules = reportEntries;
        } else {
            fulfilledRules.addAll(reportEntries);
        }

        updateRisk(reportEntries);
    }

    private void addRulesWithError(Map<ReportEntry, String> rulesWithError) {
        if (this.rulesWithError == null) {
            this.rulesWithError = rulesWithError;
        } else {
            this.rulesWithError.putAll(rulesWithError);
        }

        updateRisk(rulesWithError.keySet());
    }

    private void updateRisk(Set<ReportEntry> reportEntries) {
        if (totalRiskScore < 100) {
            recalculateRiskScore(reportEntries);
            evaluateFlag();
        }
    }

    private void recalculateRiskScore(Set<ReportEntry> reportEntries) {
        int entriesRiskScore = reportEntries.stream()
                .mapToInt(ReportEntry::getScore)
                .sum();
        totalRiskScore = Math.min(totalRiskScore + entriesRiskScore, 100);
    }

    private void evaluateFlag() {
        if (totalRiskScore < 5) {
            flag = SAFE;
        } else if (totalRiskScore < 50) {
            flag = WARNING;
        } else {
            flag = HIGH_RISK;
        }
    }

}
