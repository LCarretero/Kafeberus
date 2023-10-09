package com.hiberus.service;

import com.hiberus.avro.OrderKey;
import com.hiberus.avro.OrderValue;
import com.hiberus.dto.OrderDTO;
import com.hiberus.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderService {
    @Autowired
    private KafkaTemplate<OrderKey, OrderValue> kafkaTemplate;

    public OrderDTO makeAnOrder(OrderValue orderValue) {
        send(orderValue);
        return OrderMapper.INSTANCE.mapToDTO(orderValue);
    }

    private void send(OrderValue orderValue) {
        OrderKey key = OrderKey.newBuilder().setId(2184194).build();

        OrderValue value = OrderValue.newBuilder()
                .setIdMesa(orderValue.getIdMesa())
                .setIdProduct(orderValue.getIdProduct())
                .setIdUser(orderValue.getIdUser())
                .setTimeStamp(Instant.now().toString())
                .build();

        kafkaTemplate.send("order-by-table", key, value);
    }
}
