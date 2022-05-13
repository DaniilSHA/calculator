package com.example.server.service;

import com.example.server.dto.UnorderedReportDto;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
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

    public Flux<String> calculateUnordered(String functionFirst, String functionSecond, int iterations) {
        startUnorderedFunctionIterations(1,functionFirst, iterations);
        startUnorderedFunctionIterations(2,functionSecond, iterations);
        return Flux.from(createUnorderedPublisher(iterations));
    }


    private void startUnorderedFunctionIterations (int functionNumber, String function, int iterations) {
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < iterations; i++) {

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                int finalI = i+1;
                CompletableFuture.supplyAsync(() -> {
                    long startTime = System.currentTimeMillis();


                    long endTime = System.currentTimeMillis();
                    return new UnorderedReportDto(finalI, functionNumber, 100, endTime-startTime).getReportBody();
                }).thenAccept(report -> totalResult.add(report));

            }
        });
    }

    private Publisher createUnorderedPublisher (int iterations){
        return (subscriber) -> {
            int currentNumberOfSuccessfulRequests = 0;

            while (true) {
                if (!totalResult.isEmpty()) {
                    subscriber.onNext(totalResult.get(0));
                    totalResult.remove(0);
                    if (++currentNumberOfSuccessfulRequests == iterations * 2) {
                        subscriber.onComplete();
                        break;
                    }
                }
            }
        };
    }
}
