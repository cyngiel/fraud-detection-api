package com.frauddetection.api.rule.helper;


import com.frauddetection.api.dto.Country;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleHelper {

    private static Set<Country> highRiskCountriesConfiguration;

    public static Set<Country> getHighRiskCountries() {
        if (highRiskCountriesConfiguration == null) {
            highRiskCountriesConfiguration = loadHighRiskCountriesConfiguration();
        }
        return highRiskCountriesConfiguration;
    }

    private static Set<Country> loadHighRiskCountriesConfiguration() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(RuleHelper.class.getClassLoader().getResourceAsStream("riskscore/high-risk-countries.csv"))));
        return reader.lines()
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Country(parts[0], parts[1]);
                })
                .collect(Collectors.toSet());
    }
}
