package com.hiberus.controller;

import com.hiberus.dto.OrderDTO;
import com.hiberus.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private TableService tableService;

    @PostMapping("/please/createOrder")
    public ResponseEntity<OrderDTO> makeAnOrder(@RequestParam(name = "idMesa") String idMesa, @RequestHeader(name = "Authorization") String userId, @RequestBody OrderDTO order) {
        return ResponseEntity.ok(tableService.makeAnOrder(idMesa, userId, order));
    }
}
