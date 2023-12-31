package com.hiberus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class TicketRequestApplication {
    public static void main(String[] args) {SpringApplication.run(TicketRequestApplication.class, args);
    }
}