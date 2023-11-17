package org.binaracademy.challenge_7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {
    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(5);
    }
}