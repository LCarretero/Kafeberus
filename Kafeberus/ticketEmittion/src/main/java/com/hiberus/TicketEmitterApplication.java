package com.hiberus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class TicketEmitterApplication {
    public static void main(String[] args) {SpringApplication.run(TicketEmitterApplication.class, args);
    }
}