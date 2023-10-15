package com.hiberus.controller;

import com.hiberus.dto.TicketDTO;
import com.hiberus.services.TicketRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TicketRequestController {
    @Autowired
    private TicketRequestService ticketRequestService;

    @PostMapping("/createTicket/{idTable}")
    public ResponseEntity<TicketDTO> requestTicket(@RequestParam(name = "userId") String userId, @PathVariable(name = "idTable") int idTable) {
        TicketDTO ticket = ticketRequestService.makeTicket(userId, idTable);
        return ResponseEntity.ok(ticket);
    }

}
