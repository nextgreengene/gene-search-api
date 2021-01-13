package org.gene.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
    public String country;
    public String city;
    @JsonProperty("street_address")
    public String streetAddress;
    @JsonProperty("postal_code")
    public String postalCode;
}
