package com.frauddetection.api.service.binlookup;

import com.frauddetection.api.dto.BinDetails;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import java.io.IOException;
import java.util.Optional;

/**
 * A test implementation of the `BinLookupService` used for integration testing.
 * This service provides mock responses for specific BIN numbers to simulate
 * various scenarios such as successful lookups, not found cases, and error conditions.
 * This implementation is activated only when the "test" build profile is used.
 */
@Alternative
@ApplicationScoped
@IfBuildProfile("test")
public class TestBinLookupService extends BinLookupService {

    @Override
    public Optional<BinDetails> getBinDetails(int binNumber) throws IOException {
        System.out.println("TestBinLookupService: Fetching mocked bin details for bin: " + binNumber);
        if (binNumber == 12345678) {
            BinDetails details = new BinDetails();
            BinDetails.Country country = new BinDetails.Country();
            country.setAlpha3("USA");
            details.setCountry(country);
            return Optional.of(details);
        } else if (binNumber == 99999999) {
            return Optional.empty(); // Not found scenario
        } else if (binNumber == 11111111) {
            throw new IOException("Test Exception"); // Error scenario
        }

        return Optional.empty();
    }
}