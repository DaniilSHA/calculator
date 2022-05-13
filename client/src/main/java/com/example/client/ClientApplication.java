package com.example.client;

import com.example.client.dto.UnorderedReportDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);

        WebClient client = WebClient.create("http://localhost:8080");

        Flux<String> unorderedReportDtoFlux = client.get().uri("/calculate").retrieve().bodyToFlux(String.class);

        unorderedReportDtoFlux.subscribe(System.out::println);

    }

}
