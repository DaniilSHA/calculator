package com.example.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
