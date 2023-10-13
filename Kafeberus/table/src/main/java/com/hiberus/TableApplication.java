package com.hiberus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class TableApplication {
    public static void main(String[] args) {
        SpringApplication.run(TableApplication.class, args);
    }
}