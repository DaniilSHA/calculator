package com.example.server.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderedReport implements Report{

    private String iterationNumber;

    private String firstFunctionResult;
    private String calculateTimeFirstFunction;
    private String countFutureResultFunctionFirst;

    private String secondFunctionResult;
    private String calculateTimeSecondFunction;
    private String countFutureResultFunctionSecond;


    @Override
    public String getReportBody() {
        return String.format("[%s;%s;%s;%s;%s;%s;%s]",
                iterationNumber,
                firstFunctionResult,
                calculateTimeFirstFunction,
                countFutureResultFunctionFirst,
                secondFunctionResult,
                calculateTimeSecondFunction,
                countFutureResultFunctionSecond);
    }
}
