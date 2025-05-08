package com.frauddetection.api.service.mc;

import com.frauddetection.api.dto.BinDetails;
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
import java.util.Optional;

@ApplicationScoped
public class MastercardClientService {

    private static final Logger LOGGER = Logger.getLogger(MastercardClientService.class);
    private static final String BASE_URL = "https://sandbox.api.mastercard.com/bin-resources";
    private static final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Inject
    MCSigner signer;

    @Inject
    RequestContext requestContext;

    public Request.Builder getRequest(String url, RequestBody body) {
        Headers headers = new Headers.Builder()
                .add("X-Request-ID", requestContext.getRequestId())
                .build();

        return new Request.Builder()
                .url(BASE_URL + url)
                .headers(headers)
                .post(body);
    }

    public String send(Request request) throws IOException {
        LOGGER.infof("Sending request to %s", request.url());

        try (Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                LOGGER.errorf("Error fetching bin details: %s", response.message());
                throw new IOException("Error fetching bin details");
            }
            ResponseBody responseBody = response.body();
            if(responseBody != null) {
                String responseString = responseBody.string();
                LOGGER.infof("Response from %s: %s", request.url(), response.code());
                return responseString;
            }
            return null;
        }
    }
}
