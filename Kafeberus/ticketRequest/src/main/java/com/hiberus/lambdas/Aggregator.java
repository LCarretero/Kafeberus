package com.hiberus.lambdas;

import com.hiberus.avro.OrderValue;
import com.hiberus.avro.TableKey;
import com.hiberus.avro.TicketValue;
import org.springframework.stereotype.Component;

import java.util.TreeMap;
import java.util.UUID;

@Component
public class Aggregator implements org.apache.kafka.streams.kstream.Aggregator<TableKey, OrderValue, TicketValue> {
    @Override
    public TicketValue apply(TableKey tableKey, OrderValue orderValue, TicketValue ticketValue) {
        TreeMap<String, Integer> mergedMap = new TreeMap<>(ticketValue.getMapOfProducts());

        orderValue.getMapOfProducts().forEach((product, quantity) -> {
            mergedMap.merge(product, quantity, Integer::sum);
        });

        ticketValue = TicketValue.newBuilder().setIdTicket(UUID.randomUUID().toString())
                .setIdUser(orderValue.getIdUser())
                .setMapOfProducts(mergedMap)
                .build();
        return ticketValue;
    }
}
