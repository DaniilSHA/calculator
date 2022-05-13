package com.example.server.service;

import com.example.server.dto.UnorderedReportDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Scope("prototype")
public class CalculationService {

    @Value("${delay}")
    private int delay;

    public Flux<UnorderedReportDto> calculate(String functionFirst, String functionSecond, int count) {

        List<UnorderedReportDto> list = new ArrayList<>();
        Collections.addAll(list, new UnorderedReportDto(1,1,2,2));
        Collections.addAll(list, new UnorderedReportDto(2,1,22,15));
        Collections.addAll(list, new UnorderedReportDto(3,1,112,2));

        return Flux.fromIterable(list).delayElements(Duration.ofSeconds(2));


//        Flux.<UnorderedReportDto>generate(sink -> {
//
//            sink.next()})
//
//        Flux<UnorderedReportDto>.fromCallable();
//
//        Flux.from()
//
//        Flux.from(Mono.fromCallable())
//
//        Flux.<UnorderedReportDto>generate(
//                () -> new UnorderedReportDto(),
//                (state, sink) -> {
//                    if (state > count) {
//                        sink.complete();
//                    } else {
//
//
//                        sink.next(new UnorderedReportDto());
//                    }
//                    return state++;
//                }).va;


    }

}
