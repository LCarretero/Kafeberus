package com.hiberus;

import org.springframework.boot.SpringApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableKafka
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}