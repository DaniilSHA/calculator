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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
@RequestMapping()
public class CalculateController implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @GetMapping("/calculate")
    public Flux<UnorderedReportDto> calculateUnOrdered() {

        CalculationService calculationService = applicationContext.getBean(
                "calculationService",
                CalculationService.class);

        Flux<UnorderedReportDto> calculate = calculationService.calculate("", "", 2);

        return calculate;
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
