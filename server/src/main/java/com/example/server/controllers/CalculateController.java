package com.example.server.controllers;

import com.example.server.dto.UnorderedReportDto;
import com.example.server.service.CalculationService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class CalculateController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @GetMapping(value = "/calculate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> calculateUnOrdered() {

        CalculationService calculationService = applicationContext.getBean(
                "calculationService",
                CalculationService.class);

        Flux<String> calculate = calculationService.calculateUnordered(
                "function calc (n) {return 25/n}", "function calc (n) {return 25/n}", 4);

        calculate.subscribe(e -> System.out.println(e + " " + Thread.currentThread().getName()));

        return calculate;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
