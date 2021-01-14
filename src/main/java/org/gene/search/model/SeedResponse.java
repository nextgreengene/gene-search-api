package org.gene.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeedResponse {

    public String name;
    public String email;
    public String description;
    public String phone;
    public Address address;
    public Double score;
    public String type;
    public String industry;
    @JsonProperty("co2_type")
    public String co2Type;
    @JsonProperty("co2_monthly")
    public Double co2Monthly;
    @JsonProperty("country_co2_monthly_share")
    public Double countryCo2MonthlyShare;
}
