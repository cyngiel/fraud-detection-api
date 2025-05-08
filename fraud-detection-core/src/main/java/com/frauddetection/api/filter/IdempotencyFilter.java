package com.frauddetection.api.filter;

import com.frauddetection.api.repository.TransactionRepository;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class IdempotencyFilter implements ContainerRequestFilter {

    @Inject
    Logger LOGGER;

    @Inject
    TransactionRepository transactionRepository;


    /**
     * Filters incoming HTTP requests to detect and handle repetitive violations.
     * Checks if the request contains a unique "X-Request-ID" header and determines
     * whether the request has already been processed. If a repetitive request is detected,
     * the request is aborted with a 409 Conflict response.
     *
     * @param requestContext the context of the incoming HTTP request
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String requestId = requestContext.getHeaderString("X-Request-ID");

        if (requestId == null || requestId.isEmpty() || requestContext.getUriInfo().getPath().contains("/hello")) {
            return;
        }

        if (repetitiveRequest(requestId)) {
            LOGGER.warnf("Repetitive request detected for Request-ID: %s", requestId);
            requestContext.abortWith(
                    Response.status(Response.Status.CONFLICT)
                            .entity("Request has already been processed for ID: " + requestId)
                            .build()
            );
        } else {
            invalidateRepetitiveRequestCache(requestId);
        }
    }

    @CacheResult(cacheName = "repetitive-request-cache")
    public boolean repetitiveRequest(String requestId) {
        System.out.println("Checking for repetitive request with Request-ID: " + requestId);
        return transactionRepository.findByXRequestID(requestId) != null;
    }

    @CacheInvalidate(cacheName = "repetitive-request-cache")
    public void invalidateRepetitiveRequestCache(String requestId) {
        System.out.println("Invalidating repetitive request cache for Request-ID: " + requestId);
        LOGGER.debugf("Removing repetitive request cache with Request-ID: %s", requestId);
    }
}