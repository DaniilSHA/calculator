package com.example.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnorderedReportDto {

    private int iterationNumber;
    private int functionNumber;
    private double result;
    private int calculateTime;

}
