package com.hiberus.service;

import com.hiberus.avro.OrderValue;
import com.hiberus.avro.TableKey;
import com.hiberus.dto.TicketDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class TicketService {
    @Autowired
    private KafkaTemplate<TableKey, OrderValue> kafkaTemplate;
    private final Map<String, TreeMap<String, Integer>> productMap = new TreeMap<>();

    public TicketDTO makeTicket(String userId, String idMesa) {
        TableKey key = TableKey.newBuilder().setId(idMesa).build();
        TreeMap<String, Integer> products = productMap.get(idMesa);

        OrderValue value = OrderValue.newBuilder()
                .setMapOfProducts(products)
                .setIdUser(userId)
                .build();
        kafkaTemplate.send("ticket-created", key, value);
        return new TicketDTO(idMesa, userId);
    }

    @KafkaListener(topics = "orders")
    private void ticketConsumer(ConsumerRecord<TableKey, OrderValue> order) {
        String idMesa = order.key().getId();
        TreeMap<String, Integer> product = new TreeMap<>(order.value().getMapOfProducts());
        TreeMap<String, Integer> productsInTable = productMap.get(idMesa);
        if (productsInTable == null)
            productMap.put(idMesa, product);
        else {
            for (Map.Entry<String, Integer> a : product.entrySet()) {
                productsInTable.merge(a.getKey(), a.getValue(), Integer::sum);
            }
        }
    }

}
