package com.hiberus.controller;

import com.hiberus.dto.TicketDTO;
import com.hiberus.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @PostMapping("/createTicket/{idTable}")
    public ResponseEntity<TicketDTO> makeTicket(@RequestParam(name = "userId") String userId, @PathVariable(name = "idTable") String idTable) {
        return ResponseEntity.ok(ticketService.makeTicket(userId, idTable));
    }

}
