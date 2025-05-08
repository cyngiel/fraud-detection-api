package com.frauddetection.api.interceptor;

import com.frauddetection.api.service.RequestLogService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Provider
public class ResourceRequestInterceptor implements ContainerRequestFilter {

    @Inject
    RequestLogService requestLogService;

    /**
     * Intercepts the request and logs the request details.
     *
     * @param requestContext the request context
     * @throws IOException if an I/O error occurs
     */
    @Override
    @Transactional
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getRequestUri().toString();

        InputStream entityStream = requestContext.getEntityStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        entityStream.transferTo(baos);
        String requestBody = baos.toString();

        requestContext.setEntityStream(new ByteArrayInputStream(baos.toByteArray()));
        requestLogService.logRequest(method, uri, requestBody);
    }
}
