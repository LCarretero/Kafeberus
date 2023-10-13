package com.hiberus.controller;

import com.hiberus.dto.TicketDTO;
import com.hiberus.services.TicketEmmiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TicketController {
    @Autowired
    private TicketEmmiterService ticketEmmiterService;
    @PostMapping("/createTicket/{idTable}")
    public ResponseEntity<TicketDTO> makeTicket(@RequestParam(name = "userId") String userId, @PathVariable(name = "idTable") String idTable) {
        return ResponseEntity.ok(ticketEmmiterService.makeTicket(userId, idTable));
    }

}
