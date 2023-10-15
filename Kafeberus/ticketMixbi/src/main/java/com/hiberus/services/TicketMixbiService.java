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
public class TicketMixbiService {

    @Bean
    public BiFunction<KStream<TableKey, UserInTicketValue>, KStream<TableKey, ProductsInTicketValue>, KStream<TicketKey, FinalTicket>> process() {
        return (userStream, productStream) -> {
            KTable<TicketKey, UserInTicketValue> userKTable = createUserKTable(userStream);
            KTable<TicketKey, ProductsInTicketValue> productKTable = createProductKTable(productStream);
            return userKTable
                    .join(productKTable,
                            (userInTicketValue, productsInTicketValue) -> createFinalTicket(userInTicketValue, productsInTicketValue))
                    .toStream()
                    .peek((k, v) -> log.info("k{} ---- v{}", k, v));
        };
    }

    private KTable<TicketKey, UserInTicketValue> createUserKTable(KStream<TableKey, UserInTicketValue> userStream) {
        return userStream.selectKey((k, v) -> TicketKey.newBuilder()
                .setIdTicket(v.getIdTicket())
                .build())
                .toTable(Named.as("USER_TICKET"), Materialized.as("USER_TICKET_CHANGELOG"));
    }

    private KTable<TicketKey, ProductsInTicketValue> createProductKTable(
            KStream<TableKey, ProductsInTicketValue> productStream) {
        return productStream.selectKey((k, v) -> TicketKey.newBuilder()
                .setIdTicket(v.getIdTicket())
                .build())
                .toTable(Named.as("PRODUCTS_TICKET"), Materialized.as("PRODUCTS_TICKET_CHANGELOG"));
    }

    private FinalTicket createFinalTicket(UserInTicketValue userInTicketValue,
            ProductsInTicketValue productsInTicketValue) {
        return FinalTicket.newBuilder()
                .setMapOfProducts(productsInTicketValue.getMapOfProducts())
                .setIdUser(userInTicketValue.getIdUser())
                .setRewarded(userInTicketValue.getRewarded())
                .setPrice(userInTicketValue.getRewarded() ? productsInTicketValue.getTotalPrice() - 5
                        : productsInTicketValue.getTotalPrice())
                .setTimeStamp(Instant.now().toString())
                .build();
    }
}
