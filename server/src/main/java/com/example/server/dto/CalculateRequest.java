package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CalculateRequest {

    String firstFunction;
    String secondFunction;
    int iterations;
    String order;

    public CalculateRequest(@JsonProperty("firstFunction") String firstFunction,
                            @JsonProperty("secondFunction") String secondFunction,
                            @JsonProperty("itr") int iterations,
                            @JsonProperty("order") String order) {
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
        this.iterations = iterations;
        this.order = order;
    }

}
