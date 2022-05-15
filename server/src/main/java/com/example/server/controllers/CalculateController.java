package com.example.server.controllers;

import com.example.server.dto.CalculateRequest;
import com.example.server.exceptions.InvalidInputParamException;
import com.example.server.service.CalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
@Slf4j
public class CalculateController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @PostMapping(value = "/calculate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> calculate(@RequestBody CalculateRequest request) {

        log.info("CONTROLLER RECEIVE REQUEST:" + request);

        CalculationService calculationService = applicationContext.getBean(
                "calculationService",
                CalculationService.class);

        return calculationService.calculate(
                request.getOrder(),
                request.getFirstFunction(),
                request.getSecondFunction(),
                request.getIterations()
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
