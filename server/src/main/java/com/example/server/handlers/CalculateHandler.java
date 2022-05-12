package com.example.server.handlers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class CalculateHandler {

    public Mono<ServerResponse> calculateOrdered(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("first"));
    }

    public Mono<ServerResponse> calculateUnOrdered(ServerRequest request) {


        return ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue("second"));
    }
}
