package com.example.server.controllers;

import com.example.server.dto.CalculateRequest;
import com.example.server.exceptions.InvalidInputParamException;
import com.example.server.service.CalculationService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class CalculateController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @PostMapping(value = "/calculate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> calculate(@RequestBody CalculateRequest request) {

        System.out.println(request);

        CalculationService calculationService = applicationContext.getBean(
                "calculationService",
                CalculationService.class);

        switch (request.getOrder()) {
            case "unordered" : {
                Flux<String> stringFlux = calculationService.calculateUnordered(
                        request.getFirstFunction(),
                        request.getSecondFunction(),
                        request.getIterations());
                return stringFlux;
            }
            case "ordered" : return calculationService.calculateOrdered(
                    request.getFirstFunction(),
                    request.getSecondFunction(),
                    request.getIterations()
            );
            default: throw new InvalidInputParamException("INVALID ORDER PARAM");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
