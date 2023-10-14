package com.hiberus.controller;

import com.hiberus.dto.TicketDTO;
import com.hiberus.services.TicketEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TicketController {
    @Autowired
    private TicketEmitterService ticketEmitterService;

    @PostMapping("/createTicket/{idTable}")
    public ResponseEntity<TicketDTO> makeTicket(@RequestParam(name = "userId") String userId, @PathVariable(name = "idTable") String idTable) {
        TicketDTO ticket = ticketEmitterService.makeTicket(userId, idTable);
        return ResponseEntity.ok(ticket);
    }

}
