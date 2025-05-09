package com.frauddetection.api.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
public class ProcessedTransaction {

    @Id
    @GeneratedValue
    private long id;
    @CreationTimestamp
    private Instant timestamp;

    private int binNumber;
    private String countryCode;
    private double amount;
    private int riskScore;
    private String xRequestID;

    public ProcessedTransaction(int binNumber, String countryCode, double amount, int riskScore, String xRequestID) {
        this.binNumber = binNumber;
        this.countryCode = countryCode;
        this.amount = amount;
        this.riskScore = riskScore;
        this.xRequestID = xRequestID;
    }
}
