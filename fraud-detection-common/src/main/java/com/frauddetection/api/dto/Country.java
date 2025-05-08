package com.frauddetection.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Country {
    private String code;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return code.equals(country.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
