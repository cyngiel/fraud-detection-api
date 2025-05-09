package com.frauddetection.api.service;

import com.frauddetection.api.repository.entity.RequestLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * Service for logging incoming requests.
 */
@ApplicationScoped
public class RequestLogService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void logRequest(String method, String uri, String requestBody) {
        RequestLog log = new RequestLog();
        log.setMethod(method);
        log.setUri(uri);
        log.setRequestBody(requestBody);
        entityManager.persist(log);
    }
}
