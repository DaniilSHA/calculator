package com.example.server;

import com.example.server.dto.UnorderedReportDto;
import com.example.server.service.CalculationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServerApplication.class, args);

        Thread.sleep(10);
        CalculationService.totalResult.forEach(System.out::println);


    }

}
