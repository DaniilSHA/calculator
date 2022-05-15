package com.example.server.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderedReport implements Report{

    private int iterationNumber;

    private double firstFunctionResult;
    private long calculateTimeFirstFunction;
    private int countFutureResultFunctionFirst;

    private double secondFunctionResult;
    private long calculateTimeSecondFunction;
    private int countFutureResultFunctionSecond;


    @Override
    public String getReportBody() {
        return String.format("[%d;%.2f;%d;%d;%.2f;%d;%d]",
                iterationNumber,
                firstFunctionResult,
                calculateTimeFirstFunction,
                countFutureResultFunctionFirst,
                secondFunctionResult,
                calculateTimeSecondFunction,
                countFutureResultFunctionSecond);
    }
}
