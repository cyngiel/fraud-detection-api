package com.frauddetection.api.report;

public interface ReportEntry {
    int getScore();
    String getRuleName();
    String getRuleDescription();
}
