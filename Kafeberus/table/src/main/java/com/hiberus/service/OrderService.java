package com.hiberus.service;


import com.hiberus.avro.OrderValue;
import com.hiberus.avro.TableKey;
import com.hiberus.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
public class OrderService {
    @Autowired
    private KafkaTemplate<TableKey, OrderValue> kafkaTemplate;

    public OrderDTO makeAnOrder(String idTable, String userId, OrderDTO orderValue) {
        send(idTable, userId, orderValue);
        return orderValue;
    }

    private void send(String idTable, String userId, OrderDTO orderValue) {
        TreeMap<String, Integer> products = new TreeMap<>();
        products.put(orderValue.productName(), orderValue.quantity());

        TableKey key = TableKey.newBuilder().setIdTable(idTable).build();

        OrderValue value = OrderValue.newBuilder()
                .setIdUser(userId)
                .setMapOfProducts(products)
                .build();

        kafkaTemplate.send("order-by-table", key, value);
    }
}
