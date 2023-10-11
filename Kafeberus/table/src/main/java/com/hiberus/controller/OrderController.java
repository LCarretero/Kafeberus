package com.hiberus.controller;

import com.hiberus.avro.OrderValue;
import com.hiberus.dto.OrderDTO;
import com.hiberus.mapper.OrderMapper;
import com.hiberus.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<OrderDTO> makeAnOrder(@RequestParam(name = "idMesa") int idMesa, @RequestBody OrderDTO order) {
        OrderValue orderValue = OrderMapper.INSTANCE.mapToModel(order);
        return ResponseEntity.ok(orderService.makeAnOrder(idMesa,orderValue));
    }
}
