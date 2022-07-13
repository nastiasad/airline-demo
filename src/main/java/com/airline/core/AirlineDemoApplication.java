package com.airline.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class AirlineDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineDemoApplication.class, args);
    }
}
