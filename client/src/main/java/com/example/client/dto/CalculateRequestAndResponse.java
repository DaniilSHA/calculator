package com.example.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CalculateRequestAndResponse {

    String firstFunction;
    String secondFunction;
    int iterations;
    String order;

    public CalculateRequestAndResponse(@JsonProperty("firstFunction") String firstFunction,
                                       @JsonProperty("secondFunction") String secondFunction,
                                       @JsonProperty("itr") int iterations,
                                       @JsonProperty("order") String order) {
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
        this.iterations = iterations;
        this.order = order;
    }

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
