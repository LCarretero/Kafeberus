package com.hiberus.service;

import com.hiberus.avro.OrderKey;
import com.hiberus.avro.OrderValue;
import com.hiberus.dto.OrderDTO;
import com.hiberus.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private KafkaTemplate<OrderKey, OrderValue> kafkaTemplate;

    public OrderDTO makeAnOrder(int idMesa, OrderValue orderValue) {
        send(idMesa, orderValue);
        return OrderMapper.INSTANCE.mapToDTO(orderValue);
    }

    private void send(int idMesa, OrderValue orderValue) {
        OrderKey key = OrderKey.newBuilder().setIdMesa(idMesa).build();

        OrderValue value = OrderValue.newBuilder()
                .setProductName(orderValue.getProductName())
                .setIdUser(orderValue.getIdUser())
                .build();

        kafkaTemplate.send("order-by-table", key, value);
    }
}
