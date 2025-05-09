package com.frauddetection.api.service.mc;

import com.frauddetection.api.filter.RequestContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jboss.logging.Logger;

import java.io.IOException;

/**
 * Service for interacting with the Mastercard API.
 * <p>
 * This service is responsible for sending requests to the Mastercard API and handling the responses.
 * It uses OkHttpClient for making HTTP requests and includes methods for signing requests and processing responses.
 * </p>
 */
@ApplicationScoped
public class MastercardClientService {

    private static final Logger LOGGER = Logger.getLogger(MastercardClientService.class);
    private static final String BASE_URL = "https://sandbox.api.mastercard.com/bin-resources";
    private static final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Inject
    RequestContext requestContext;

    /**
     * Creates a new request builder for the specified URL and request body.
     *
     * @param url  the URL to send the request to
     * @param body the request body
     * @return Request.Builder object
     */
    public Request.Builder getRequest(String url, RequestBody body) {
        Headers headers = new Headers.Builder()
                .add("X-Request-ID", requestContext.getRequestId())
                .build();

        return new Request.Builder()
                .url(BASE_URL + url)
                .headers(headers)
                .post(body);
    }

    /**
     * Sends the specified request to the Mastercard API and returns the response as a string.
     * Returns null if the response body is empty.
     *
     * @param request the request to send
     * @return the response body as a string
     * @throws IOException if an error occurs while sending the request or processing the response
     */
    public String send(Request request) throws IOException {
        LOGGER.infof("Sending request to %s", request.url());

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LOGGER.errorf("Error fetching bin details: %s", response.message());
                throw new IOException("Error fetching bin details");
            }
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseString = responseBody.string();
                LOGGER.infof("Response from %s: %s", request.url(), response.code());
                return responseString;
            }
            return null;
        }
    }
}
