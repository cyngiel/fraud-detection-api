package com.frauddetection.api.rule;

public class RuleEvaluationException extends Exception {
    public RuleEvaluationException(String message) {
        super(message);
    }

    public RuleEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
