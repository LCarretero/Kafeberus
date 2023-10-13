package com.hiberus.services;


import com.hiberus.avro.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

@Service
@Slf4j
public class TicketMixbiService {

    @Bean
    public BiFunction<KStream<TableKey, UserInTicketValue>, KStream<TableKey, ProductsInTicketValue>, KStream<TicketKey, FinalTicket>> process() {
        return (userStream, productStream) -> {
            KTable<TicketKey, UserInTicketValue> userKTable = createUserKTable(userStream);
            AtomicReference<Integer> counter = new AtomicReference<>(0);
            KTable<TicketKey, ProductsInTicketValue> productKTable = createProductKTable(productStream);
            userKTable.toStream().peek((k, v) -> log.info("[UserTable] Sending message with key: {} and value: {}", k, v));
            productKTable.toStream().peek((k, v) -> log.info("[ProductTable] Sending message with key: {} and value: {}", k, v));
            var macro = userKTable.leftJoin(productKTable, (userInTicketValue, productsInTicketValue) -> FinalTicket.newBuilder()
                            .setMapOfProducts(productsInTicketValue.getMapOfProducts())
                            .setIdUser(userInTicketValue.getIdUser())
                            .setPrice(productsInTicketValue.getTotalPrice())
                            .setTimeStamp(Instant.now().toString())
                            .build())
                    .toStream();
            macro.peek((k, v) -> log.info("MACRO COUNTER "+counter.getAndSet(counter.get() + 1)));
            return macro.peek((k, v) -> log.info("[joiner] Sending message with key: {} and value: {}", k, v));
        };
    }

    private KTable<TicketKey, UserInTicketValue> createUserKTable(KStream<TableKey, UserInTicketValue> userStream) {
        return userStream.selectKey((k, v) -> TicketKey.newBuilder()
                        .setIdTicket(v.getIdTicket())
                        .build())
                .toTable(Named.as("USER_TICKET"), Materialized.as("aa"));
    }

    private KTable<TicketKey, ProductsInTicketValue> createProductKTable(KStream<TableKey, ProductsInTicketValue> productStream) {
        return productStream.selectKey((k, v) -> TicketKey.newBuilder()
                        .setIdTicket(v.getIdTicket())
                        .build())
                .toTable(Named.as("PRODUCTS_TICKET"), Materialized.as("bb"));
    }

}
