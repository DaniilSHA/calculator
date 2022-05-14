package com.example.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculateResponse {

    String firstFunction;
    String secondFunction;
    int iterations;
    String order;

    @JsonProperty
    public String getFirstFunction() {
        return firstFunction;
    }

    @JsonProperty
    public String getSecondFunction() {
        return secondFunction;
    }

    @JsonProperty("itr")
    public int getIterations() {
        return iterations;
    }

    @JsonProperty
    public String getOrder() {
        return order;
    }
}
