package com.frauddetection.api.dto.binlookup;

import lombok.Data;

@Data
public class BinLookupResponse { //todo po co to

    String binNumber;

    public static class Builder {
        private String binNumber;

        public Builder binNumber(String binNumber) {
            this.binNumber = binNumber;
            return this;
        }

        public BinLookupResponse build() {
            BinLookupResponse response = new BinLookupResponse();
            response.setBinNumber(this.binNumber);
            return response;
        }
    }
}


