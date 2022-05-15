package com.example.server.service;

import com.example.server.reports.OrderedReport;
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
import java.util.function.Consumer;

@Service
@Scope("prototype")
@Slf4j
public class CalculationService implements ApplicationContextAware {

    @Value("${delay}")
    private int delay;

    private ApplicationContext applicationContext;

    private CopyOnWriteArrayList<String> totalResultAccumulator = new CopyOnWriteArrayList<>();
    private LinkedBlockingQueue<String> firstFunctionResultAccumulator = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<String> secondFunctionResultAccumulator = new LinkedBlockingQueue<>();

    public Flux<String> calculate(String order, String functionFirst, String functionSecond, int iterations) {

        if (functionFirst.trim().equals("") || functionSecond.trim().equals("")) return createErrorPublisher("EMPTY FUNCTION ERROR");
        if (iterations==0) return createErrorPublisher("EMPTY ITERATIONS ERROR");

        switch (order) {
            case "unordered" : {
                startFunctionIterations(1, functionFirst, iterations, report -> totalResultAccumulator.add(report));
                startFunctionIterations(2, functionSecond, iterations, report -> totalResultAccumulator.add(report));
                return createUnorderedPublisher(iterations);
            }
            case "ordered" : {
                startFunctionIterations(1, functionFirst, iterations, report -> firstFunctionResultAccumulator.add(report));
                startFunctionIterations(2, functionSecond, iterations, report -> secondFunctionResultAccumulator.add(report));
                return createOrderedPublisher(iterations);
            }
            default: return createErrorPublisher("INVALID ORDER PARAM");
        }
    }

    private void startFunctionIterations(int functionNumber, String function, int iterations, Consumer<String> consumer) {
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
                        .thenAccept(consumer);
            }
        });
    }

    private Flux<String> createUnorderedPublisher(int iterations) {

        Sinks.Many<String> publisher = Sinks.many().multicast().onBackpressureBuffer();

        CompletableFuture.runAsync( () -> {
            int currentNumberOfSuccessfulRequests = 0;
            while (true) {
                if (!totalResultAccumulator.isEmpty()) {
                    publisher.emitNext(totalResultAccumulator.get(0), (signalType, emitResult) -> false);
                    totalResultAccumulator.remove(0);
                    if (++currentNumberOfSuccessfulRequests == iterations * 2) {
                        publisher.emitComplete((signalType, emitResult) -> false);
                        break;
                    }
                }
            }
        });

        return publisher.asFlux();
    }

    private Flux<String> createOrderedPublisher(int iterations) {

        Sinks.Many<String> publisher = Sinks.many().multicast().onBackpressureBuffer();

        CompletableFuture.runAsync( () -> {
            int currentNumberOfSuccessfulRequests = 0;
            while (true) {

                if (!firstFunctionResultAccumulator.isEmpty() && !secondFunctionResultAccumulator.isEmpty()) {

                    String unorderedReportFromFirstFunction = firstFunctionResultAccumulator.remove();
                    String unorderedReportFromSecondFunction = secondFunctionResultAccumulator.remove();

                    String commonReport = convertUnorderedReportsToOrderReport(
                            unorderedReportFromFirstFunction,
                            unorderedReportFromSecondFunction,
                            ++currentNumberOfSuccessfulRequests);

                    publisher.emitNext(commonReport, (signalType, emitResult) -> false);
                    if (currentNumberOfSuccessfulRequests == iterations) {
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

    private String convertUnorderedReportsToOrderReport(String reportFirst,
                                                        String reportSecond,
                                                        int currentNumberOfSuccessfulRequests){

        String[] dataFromFirstReport;
        String[] dataFromSecondReport;

        if (reportFirst.contains("INVALID FUNCTION")){
            dataFromFirstReport = new String[]{String.valueOf(currentNumberOfSuccessfulRequests),
                    "1",
                    "INVALID FUNCTION #1",
                    "-1"
            };
        } else {
            dataFromFirstReport = reportFirst.substring(1,reportFirst.length()-1).split(";");
        }

        if (reportSecond.contains("INVALID FUNCTION")){
            dataFromSecondReport = new String[]{String.valueOf(currentNumberOfSuccessfulRequests),
                    "1",
                    "INVALID FUNCTION #2",
                    "-1"
            };
        } else {
            dataFromSecondReport = reportSecond.substring(1,reportSecond.length()-1).split(";");
        }

        return OrderedReport.builder()
                .iterationNumber(String.valueOf(currentNumberOfSuccessfulRequests))
                .firstFunctionResult(dataFromFirstReport[2])
                .calculateTimeFirstFunction(dataFromFirstReport[3])
                .countFutureResultFunctionFirst(String.valueOf(firstFunctionResultAccumulator.size()))
                .secondFunctionResult(dataFromSecondReport[2])
                .calculateTimeSecondFunction(dataFromSecondReport[3])
                .countFutureResultFunctionSecond(String.valueOf(secondFunctionResultAccumulator.size()))
                .build()
                .getReportBody();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
