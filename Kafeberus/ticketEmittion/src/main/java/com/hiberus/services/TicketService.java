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
public class TicketService {
    @Autowired
    private KafkaTemplate<TableKey, TicketValue> kafkaTemplate;
    private final Map<String, TreeMap<String, Integer>> productMap = new TreeMap<>();

    public TicketDTO makeTicket(String userId, String idTable) {
        TableKey key = TableKey.newBuilder().setIdTable(idTable).build();
        TreeMap<String, Integer> products = productMap.get(idTable);

        TicketValue value = TicketValue.newBuilder()
                .setMapOfProducts(products)
                .setIdTicket(UUID.randomUUID().toString())
                .setIdUser(userId)
                .build();
        kafkaTemplate.send("ticket-created", key, value);
        productMap.remove(idTable);
        return new TicketDTO(idTable, userId);
    }

    @Bean
    public Consumer<KStream<TableKey, OrderValue>> process() {
        return tableKeyOrderValueKStream -> tableKeyOrderValueKStream
                .peek((k, v) -> {
                    log.info("Received message with key: {} and value {}", k, v);
                    String idTable = k.getIdTable();
                    TreeMap<String, Integer> product = new TreeMap<>(v.getMapOfProducts());
                    TreeMap<String, Integer> productsInTable = productMap.get(idTable);
                    if (productsInTable == null)
                        productMap.put(idTable, product);
                    else {
                        for (Map.Entry<String, Integer> a : product.entrySet()) {
                            productsInTable.merge(a.getKey(), a.getValue(), Integer::sum);
                        }
                    }
                });
    }
}
