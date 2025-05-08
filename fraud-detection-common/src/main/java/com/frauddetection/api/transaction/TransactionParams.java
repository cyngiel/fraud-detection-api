package com.frauddetection.api.transaction;

import com.frauddetection.api.dto.BinDetails;
import io.smallrye.common.constraint.Nullable;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class TransactionParams {

    @Nullable
    private BinDetails binDetails;
    private int bin;
    private String countryCode;
    private double amount;

    public Optional<BinDetails> getBinDetails() {
        return Optional.ofNullable(binDetails);
    }
}
