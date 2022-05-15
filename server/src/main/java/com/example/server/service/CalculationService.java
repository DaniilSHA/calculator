package com.example.server.service;

import com.example.server.reports.UnorderedReport;
import com.example.server.exceptions.InvalidInputParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Scope("prototype")
@Slf4j
public class CalculationService implements ApplicationContextAware {

    @Value("${delay}")
    private int delay;

    private ApplicationContext applicationContext;

    private CopyOnWriteArrayList<String> totalResult = new CopyOnWriteArrayList<>();
    private LinkedBlockingQueue<String> firstFunctionResult = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<String> secondFunctionResult = new LinkedBlockingQueue<>();

    public Flux<String> calculateUnordered(String functionFirst, String functionSecond, int iterations) {
        if (functionFirst.trim().equals("") || functionSecond.trim().equals("")) return createErrorPublisher("EMPTY FUNCTION ERROR");
        if (iterations==0) return createErrorPublisher("EMPTY ITERATIONS ERROR");
        startUnorderedFunctionIterations(1, functionFirst, iterations);
        startUnorderedFunctionIterations(2, functionSecond, iterations);
        return createUnorderedPublisher(iterations);
    }

    public Flux<String> calculateOrdered(String firstFunction, String secondFunction, Integer iterations) {
        return null;
    }

    private void startUnorderedFunctionIterations(int functionNumber, String function, int iterations) {
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < iterations; i++) {

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int finalI = i + 1;
                CompletableFuture.supplyAsync(() -> {
                    long startTime = System.currentTimeMillis();

                    Double functionResult = 0d;
                    try {
                        ScriptEngine scriptEngine = applicationContext.getBean("scriptEngine", ScriptEngine.class);
                        functionResult = Double.valueOf(scriptEngine
                                .eval(functionConverterToCurrentIteration(function,finalI))
                                .toString());
                    } catch (ClassCastException | ScriptException e) {
                        log.error("INVALID FUNCTION #"+functionNumber);
                        throw new InvalidInputParamException("[INVALID FUNCTION #" + functionNumber+"]");
                    }

                    long endTime = System.currentTimeMillis();
                    return new UnorderedReport(finalI, functionNumber, functionResult, endTime - startTime).getReportBody();
                })
                        .exceptionally(throwable -> "[INVALID FUNCTION #"+functionNumber+"]")
                        .thenAccept(report -> totalResult.add(report));
            }
        });
    }

    private Flux<String> createUnorderedPublisher(int iterations) {

        Sinks.Many<String> publisher = Sinks.many().multicast().onBackpressureBuffer();

        CompletableFuture.runAsync( () -> {
            int currentNumberOfSuccessfulRequests = 0;
            while (true) {
                if (!totalResult.isEmpty()) {
                    publisher.emitNext(totalResult.get(0), (signalType, emitResult) -> false);
                    totalResult.remove(0);
                    if (++currentNumberOfSuccessfulRequests == iterations * 2) {
                        publisher.emitComplete((signalType, emitResult) -> false);
                        break;
                    }
                }
            }
        });

        return publisher.asFlux();
    }

    private Flux<String> createErrorPublisher(String error) {

        Sinks.Many<String> publisher = Sinks.many().multicast().onBackpressureBuffer();

        CompletableFuture.runAsync( () -> {
            publisher.emitNext("["+error+"]", (signalType, emitResult) -> false);
            publisher.emitComplete((signalType, emitResult) -> false);
        });

        return publisher.asFlux();
    }

    private synchronized String functionConverterToCurrentIteration(String function, int currentIteration) {
        String functionName = function.substring(function.indexOf("on")+3, function.indexOf("(")-1);
        return function + functionName + "(" + currentIteration + ")";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
