package com.hiberus.services;

import com.hiberus.avro.OrderValue;
import com.hiberus.avro.TableKey;
import com.hiberus.avro.TicketValue;
import com.hiberus.dto.TicketDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@Slf4j
public class TicketRequestService {

    @Autowired
    private KafkaTemplate<TableKey, TicketValue> kafkaTemplate;

    // @Autowired
    // private Initializer initializer;

    // @Autowired
    // private Aggregator aggregator;

    private final Map<String, TreeMap<String, Integer>> valueAccumulator = new TreeMap<>();

    @Bean
    public Consumer<KStream<TableKey, OrderValue>> process() {
        return inputKStream -> inputKStream
                .selectKey((k, v) -> TableKey.newBuilder().setIdTable(k.getIdTable()).build())
                .peek((k, v) -> {
                    aggregate(k.getIdTable(), new TreeMap<>(v.getMapOfProducts()));
                    log.info("Key:{} --- value:{}", k.getIdTable(), valueAccumulator.get(k.getIdTable()));
                });
    }

    public TicketDTO makeTicket(String userId, String idTable) {

        TableKey key = TableKey.newBuilder().setIdTable(idTable).build();
        TicketValue value = TicketValue.newBuilder()
                .setMapOfProducts(
                        valueAccumulator.get(idTable) == null ? new TreeMap<>() : valueAccumulator.get(idTable))
                .setIdTicket(UUID.randomUUID().toString())
                .setIdUser(userId)
                .build();

        log.info("Key:{} --- value:{}", key, value);
        kafkaTemplate.send("ticket-created", key, value);
        log.info("[ANTES DE BORRAR]  ----> {}", valueAccumulator.get(idTable));
        valueAccumulator.remove(idTable);
        log.info("[DESPUES DE BORRAR]  ----> {}", valueAccumulator.get(idTable));
        return new TicketDTO(idTable, userId);
    }

    private void aggregate(String k, TreeMap<String, Integer> v) {
        TreeMap<String, Integer> product = new TreeMap<>(v);
        TreeMap<String, Integer> productsInTable = valueAccumulator.get(k);
        if (productsInTable == null)
            valueAccumulator.put(k, product);
        else {
            for (Map.Entry<String, Integer> a : product.entrySet()) {
                productsInTable.merge(a.getKey(), a.getValue(), (v1, v2) -> v1 + v2);
            }
        }
    }

}
