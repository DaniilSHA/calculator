package com.example.client;

import com.example.client.dto.CalculateResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);

        WebClient client = WebClient.create("http://localhost:8080");

        Flux<String> unorderedReportDtoFlux = client
                .post()
                .uri("/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .body(BodyInserters.fromValue((
                        new CalculateResponse(
                                "function calc (n) {return 2+n}",
                                "function calc (n) {return 2+n}",
                                6,
                                "unordered"))
                ))
                .retrieve()
                .bodyToFlux(String.class);


        unorderedReportDtoFlux.subscribe(System.out::println);

    }

}
