package com.example.server;

import com.example.server.dto.UnorderedReportDto;
import com.example.server.service.CalculationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Collections;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public ScriptEngineManager scriptEngineManager () {
        return new ScriptEngineManager();
    }

    @Bean
    @Scope("prototype")
    public ScriptEngine scriptEngine () {
        return scriptEngineManager().getEngineByName("nashorn");
    }

}
