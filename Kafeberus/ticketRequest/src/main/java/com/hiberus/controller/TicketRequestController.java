package com.hiberus.controller;

import com.hiberus.dto.TicketDTO;
import com.hiberus.services.TicketRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Tag(name = "Ticket Request Controller", description = "API endpoints for ticket requests")
public class TicketRequestController {
    @Autowired
    private TicketRequestService ticketRequestService;

    @PostMapping("/createTicket/{idTable}")
    @Operation(
            summary = "Create a ticket",
            description = "Create a new ticket request for a table.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket request created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketDTO.class)))
            }
    )
    public ResponseEntity<TicketDTO> requestTicket(
            @RequestParam(name = "userId") String userId,
            @PathVariable(name = "idTable") int idTable
    ) {
        TicketDTO ticket = ticketRequestService.makeTicket(userId, idTable);
        return ResponseEntity.ok(ticket);
    }
}
