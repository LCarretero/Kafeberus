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
import java.util.function.BiFunction;

@Service
@Slf4j
public class TicketService {

    @Bean
    public BiFunction<KStream<TableKey, UserInTicketValue>, KStream<TableKey, ProductsInTicketValue>, KStream<TicketKey, FinalTicket>> process() {
        return (userStream, productStream) -> {
            KTable<TicketKey, UserInTicketValue> userKTable = userStream
                    .selectKey((k, v) -> TicketKey.newBuilder().setIdTicket(v.getIdTicket()).build())
                    .toTable(Named.as("USER_TICKET"), Materialized.as("CARACTERISTICAS_PRODUCTOS"));

            KTable<TicketKey, ProductsInTicketValue> productKTable = productStream
                    .selectKey((k, v) -> TicketKey.newBuilder().setIdTicket(v.getIdTicket()).build())
                    .toTable(Named.as("PRODUCTS_TICKET"), Materialized.as("PRODUCTS_TICKET"));

            return userKTable.join(productKTable,
                            (userInTicketValue, productsInTicketValue) -> FinalTicket.newBuilder()
                                    .setMapOfProducts(productsInTicketValue.getMapOfProducts())
                                    .setIdUser(userInTicketValue.getIdUser())
                                    .setPrice(productsInTicketValue.getTotalPrice())
                                    .setTimeStamp(Instant.now().toString())
                                    .build())
                    .toStream()
                    .peek((k, v) -> log.info("[joiner] Sending message with key: {} and value: {}", k, v));
        };
    }

}
