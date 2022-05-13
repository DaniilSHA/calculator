package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UnorderedReportDto implements ReportDto {

    private int iterationNumber;
    private int functionNumber;
    private double result;
    private long calculateTime;

    @Override
    public String getReportBody() {
        return String.format("%3d; %3d; %8.2f; %6d", iterationNumber, functionNumber, result, calculateTime);
    }

}
