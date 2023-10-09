package com.hiberus;

import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.ProductCRUDValue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

import static org.reflections.Reflections.log;

@SpringBootApplication
@EnableKafka
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}