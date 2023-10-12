package com.hiberus.controller;

import com.hiberus.dto.OrderDTO;
import com.hiberus.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/please/createOrder")
    public ResponseEntity<OrderDTO> makeAnOrder(@RequestParam(name = "idMesa") String idMesa, @RequestHeader(name = "Authorization") String userId, @RequestBody OrderDTO order) {
        return ResponseEntity.ok(orderService.makeAnOrder(idMesa, userId, order));
    }
}
