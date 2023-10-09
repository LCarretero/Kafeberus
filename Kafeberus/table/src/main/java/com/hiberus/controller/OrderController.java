package com.hiberus.controller;

import com.hiberus.avro.OrderValue;
import com.hiberus.dto.OrderDTO;
import com.hiberus.mapper.OrderMapper;
import com.hiberus.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/createOrder")
    public ResponseEntity<OrderDTO> makeAnOrder(OrderDTO order) {
        OrderValue orderValue = OrderMapper.INSTANCE.mapToModel(order);
        return ResponseEntity.ok(orderService.makeAnOrder(orderValue));
    }
}
