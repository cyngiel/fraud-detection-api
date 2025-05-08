package com.frauddetection.api.strategy;

public class StrategyExecutionException extends Exception {
    public StrategyExecutionException(String message) {
        super(message);
    }

    public StrategyExecutionException(Throwable cause) {
        super(cause);
    }
}
