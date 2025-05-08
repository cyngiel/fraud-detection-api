package com.frauddetection.api.filter;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;

import java.util.UUID;

/**
 * A filter that generates a unique request ID for each incoming request and adds it to the response headers.
 * The request ID is stored in the MDC (Mapped Diagnostic Context) for logging purposes.
 */
@Provider
public class RequestIdFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String REQUEST_ID_HEADER = "X-RequestID";

    @Inject
    RequestContext requestContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String requestId = requestContext.getHeaderString(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        MDC.put(REQUEST_ID_HEADER, requestId);

        this.requestContext.setRequestId(requestId);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().add(REQUEST_ID_HEADER, this.requestContext.getRequestId());

        MDC.remove(REQUEST_ID_HEADER);
    }
}
