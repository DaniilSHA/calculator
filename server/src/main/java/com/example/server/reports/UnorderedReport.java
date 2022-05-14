package com.example.server.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UnorderedReport implements Report {

    private int iterationNumber;
    private int functionNumber;
    private double result;
    private long calculateTime;

    @Override
    public String getReportBody() {
        return String.format("%3d; %3d; %8.2f; %6d", iterationNumber, functionNumber, result, calculateTime);
    }

}
