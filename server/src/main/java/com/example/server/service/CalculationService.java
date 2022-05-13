package com.example.server.service;

import com.example.server.dto.UnorderedReportDto;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Scope("prototype")
@Slf4j
public class CalculationService {

    @Value("${delay}")
    private int delay;

    public static CopyOnWriteArrayList<String> totalResult = new CopyOnWriteArrayList<>();
    private LinkedBlockingQueue<String> firstFunctionResult = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<String> secondFunctionResult = new LinkedBlockingQueue<>();

    public Flux<String> calculateUnordered(String functionFirst, String functionSecond, int count) {

        for (int i = 0; i < count; i++) {

            int finalI = i;
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return new UnorderedReportDto(finalI, 1, 100, 100).getReportBody();
            }).thenAccept(report -> totalResult.add(report));

        }

        for (int i = 0; i < count; i++) {

            int finalI = i;
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return new UnorderedReportDto(finalI, 2, 200, 200).getReportBody();
            }).thenAccept(report -> totalResult.add(report));

        }

        return Flux.from((Publisher) subscriber -> {
            int currentNumberOfSuccessfulRequests = 0;
            while (true) {
                if (!totalResult.isEmpty()) {
                    subscriber.onNext(totalResult.get(0));
                    totalResult.remove(0);
                    if (++currentNumberOfSuccessfulRequests == count*2) {
                        subscriber.onComplete();
                        break;
                    }
                }
            }
        });
    }
}
