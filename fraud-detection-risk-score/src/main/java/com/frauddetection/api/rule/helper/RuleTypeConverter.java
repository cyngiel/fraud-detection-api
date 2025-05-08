package com.frauddetection.api.rule.helper;

import com.frauddetection.api.rule.RuleType;
import org.eclipse.microprofile.config.spi.Converter;

public class RuleTypeConverter implements Converter<RuleType> {

    @Override
    public RuleType convert(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        for (RuleType type : RuleType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown rule type: " + value);
    }
}
