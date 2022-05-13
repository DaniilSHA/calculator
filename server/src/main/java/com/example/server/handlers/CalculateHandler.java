package com.example.server.handlers;

import com.example.server.dto.UnorderedReportDto;
import com.example.server.service.CalculationService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class CalculateHandler implements ApplicationContextAware {

    ApplicationContext applicationContext;

    public Mono<ServerResponse> calculateOrdered(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("first"));
    }

    public Mono<ServerResponse> calculateUnOrdered(ServerRequest request) {

        CalculationService calculationService = applicationContext.getBean(
                "calculationService",
                CalculationService.class);

//        calculationService.calculate("", "", 2);
        Flux<UnorderedReportDto> calculate = calculationService.calculate("", "", 2);

        return ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(calculate, UnorderedReportDto.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
