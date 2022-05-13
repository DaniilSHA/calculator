package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UnorderedReportDto {

    private int iterationNumber;
    private int functionNumber;
    private double result;
    private int calculateTime;

}
