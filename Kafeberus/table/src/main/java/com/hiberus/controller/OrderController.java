package com.hiberus.controller;

import com.hiberus.dto.OrderDTO;
import com.hiberus.services.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Tag(name = "Order Controller", description = "API endpoints for order management")
public class OrderController {

    @Autowired
    private TableService tableService;

    @PostMapping("/please/createOrder")
    @Operation(
            summary = "Create an order",
            description = "Create a new order for a table.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class)))
            }
    )
    public ResponseEntity<OrderDTO> makeAnOrder(
            @RequestParam(name = "idMesa") int idMesa,
            @RequestHeader(name = "Authorization") String userId,
            @RequestBody OrderDTO order
    ) {
        return ResponseEntity.ok(tableService.makeAnOrder(idMesa, userId, order));
    }
}
