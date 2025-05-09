package com.frauddetection.api.service.binlookup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frauddetection.api.dto.BinDetails;
import com.frauddetection.api.dto.mastercard.SingleLookupRequest;
import com.frauddetection.api.service.mc.MCSigner;
import com.frauddetection.api.service.mc.MastercardClientService;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.frauddetection.api.service.mc.MastercardClientService.JSON;


@ApplicationScoped
public class BinLookupService {

    private static final Logger LOGGER = Logger.getLogger(BinLookupService.class);
    private static final String singleLookupUrl = "/bin-ranges/account-searches";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    MastercardClientService mastercardClientService;

    @Inject
    MCSigner signer;

    /**
     * Fetches BIN details for a given BIN number. Calls Mastercard Bin Lookup API and caches the result.
     *
     * @param binNumber the BIN number to look up
     * @return an Optional containing the BinDetails
     * @throws IOException if an error occurs while fetching the BIN details
     * @see BinDetails
     */
    @CacheResult(cacheName = "bin-details-cache")
    public Optional<BinDetails> getBinDetails(int binNumber) throws IOException {
        LOGGER.debugf("Fetching bin details for bin: %s", binNumber);
        return fetchBinDetails(binNumber);
    }

    private Optional<BinDetails> fetchBinDetails(int binNumber) throws IOException {
        String payload = objectMapper.writeValueAsString(new SingleLookupRequest(binNumber));
        RequestBody body = RequestBody.create(payload, JSON);

        Request.Builder request = mastercardClientService.getRequest(singleLookupUrl, body);
        signer.sign(request);

        String responseString = mastercardClientService.send(request.build());
        if (responseString != null) {
            List<BinDetails> binDetails = objectMapper.readValue(responseString, new TypeReference<List<BinDetails>>() {});
            return binDetails.isEmpty() ? Optional.empty() : Optional.ofNullable(binDetails.getFirst());
        }
        return Optional.empty();
    }
}
