package com.example.server.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
        return String.format("[%d;%d;%.2f;%d]", iterationNumber, functionNumber, result, calculateTime);
    }

}
