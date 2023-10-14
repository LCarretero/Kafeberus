package com.hiberus.lambdas;

import com.hiberus.avro.TicketValue;
import org.springframework.stereotype.Component;

import java.util.TreeMap;
import java.util.UUID;

@Component
public class Initializer implements org.apache.kafka.streams.kstream.Initializer<TicketValue> {
    @Override
    public TicketValue apply() {
        return TicketValue.newBuilder()
                .setMapOfProducts(new TreeMap<>())
                .setIdUser("")
                .setIdTicket(UUID.randomUUID().toString())
                .build();
    }
}
