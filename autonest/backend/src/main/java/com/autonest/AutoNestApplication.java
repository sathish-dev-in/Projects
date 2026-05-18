package com.autonest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AutoNestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoNestApplication.class, args);
    }
}
