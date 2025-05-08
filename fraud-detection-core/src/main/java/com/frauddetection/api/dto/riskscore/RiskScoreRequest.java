package com.frauddetection.api.dto.riskscore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class RiskScoreRequest { //TODO nie dziala validacja
    @Min(value = 100000, message = "BIN must have at least 6 digits")
    @Max(value = 99999999, message = "BIN must have at most 8 digits")
    @Schema(description = "Single 6-8 digits BIN", example = "99630215")
    private int bin;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Country code must be a three-letter uppercase ISO 3166-1 alpha-3 code")
    @Schema(description = "Three-letter uppercase ISO 3166-1 alpha-3 country code", example = "AFG")
    private String countryCode;

    @Min(value = 1, message = "Transaction amount must be at least 1")
    private double transactionAmount;
}
