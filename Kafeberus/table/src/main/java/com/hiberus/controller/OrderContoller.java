package com.hiberus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/order")
public class OrderContoller {
    public ResponseEntity<OrderDTO> makeAnOrder(OrderValue order){

        return ResponseEntity.ok(OrderMapper.INSTANCE.mapToDTO(orderService.makeAnOrder(order)));
    }
}
