package com.frauddetection.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigInteger;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinDetails {
    private BigInteger lowAccountRange;
    private BigInteger highAccountRange;
    private String binNum;
    private int binLength;
    private String acceptanceBrand;
    private String ica;
    private String customerName;
    private Country country;
    private boolean localUse;
    private boolean authorizationOnly;
    private String productCode;
    private String productDescription;
    private boolean governmentRange;
    private boolean nonReloadableIndicator;
    private String anonymousPrepaidIndicator;
    private String programName;
    private String vertical;
    private String fundingSource;
    private String consumerType;
    private String cardholderCurrencyIndicator;
    private String billingCurrencyDefault;
    private String comboCardIndicator;
    private String flexCardIndicator;
    private boolean smartDataEnabled;
    private String affiliate;
    private String credentialStatus;
    private String paymentAccountType;
    private boolean gamblingBlockEnabled;

    @Data
    public static class Country {
        private String code;
        private String alpha3;
        private String name;
    }
}
