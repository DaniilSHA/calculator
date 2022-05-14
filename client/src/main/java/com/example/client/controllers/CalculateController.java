package com.example.client.controllers;

import com.example.client.dto.CalculateRequestAndResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/")
public class CalculateController {

    WebClient client;

    @Autowired
    public CalculateController(WebClient client) {
        this.client = client;
    }

    @PostMapping(value = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> calculate(@RequestBody CalculateRequestAndResponse request) {

        System.out.println(request);

        Flux<String> unorderedReportDtoFlux = client
                .post()
                .uri("/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .body(BodyInserters.fromValue((request)))
                .retrieve()
                .bodyToFlux(String.class);

        unorderedReportDtoFlux.subscribe(System.out::println);

        return null;
    }


}
