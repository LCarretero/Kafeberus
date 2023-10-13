package com.hiberus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class TicketRecordApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicketRecordApplication.class, args);
    }
}