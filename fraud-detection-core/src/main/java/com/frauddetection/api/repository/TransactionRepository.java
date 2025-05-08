package com.frauddetection.api.repository;

import com.frauddetection.api.repository.entity.ProcessedTransaction;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<ProcessedTransaction> {

    public long countByBinNumber(int binNumber) {
        return find("binNumber", binNumber).count();
    }

    public ProcessedTransaction findByBinNumberAndXRequestID(int binNumber, String xRequestID) {
        return find("binNumber = ?1 and xRequestID = ?2", binNumber, xRequestID).firstResult();
    }

    public ProcessedTransaction findByXRequestID(String xRequestID) {
        return find("xRequestID", xRequestID).firstResult();
    }
}
