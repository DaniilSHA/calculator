package com.example.server.config;

import com.example.server.handlers.CalculateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CalculateRoute {

//    @Bean
//    public RouterFunction<ServerResponse> route (CalculateHandler calculateHandler) {
//        return RouterFunctions
//                .route(
//                        GET("/calculate")
//                                .and(accept(MediaType.ALL)
//                                .and(queryParam("order", "ordered"))),
//                        calculateHandler::calculateOrdered)
//                .andRoute(
//                        GET("/calculate")
//                                .and(accept(MediaType.ALL)
//                                .and(queryParam("order", "unordered"))),
//                        calculateHandler::calculateUnOrdered);
//    }

}
