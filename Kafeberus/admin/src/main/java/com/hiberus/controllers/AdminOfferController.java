package com.hiberus.controllers;

import com.hiberus.exception.BadRequestException;
import com.hiberus.exception.UnauthorizedException;
import com.hiberus.dto.OfferDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.services.AdminOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offer")
@AllArgsConstructor
@Tag(name = "Offer Controller", description = "API endpoints for offer management")
public class AdminOfferController {

    private AdminOfferService adminOfferService;

    @PostMapping("/create")
    @Operation(summary = "Create an offer", description = "Create a new offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<OfferDTO> createOffer(@RequestHeader(name = "Authorization") String authorization, @RequestBody OfferDTO offer) {
        try {
            return ResponseEntity.ok(adminOfferService.crudOperation(authorization, offer, DbbVerbs.POST));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete an offer", description = "Delete an existing offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<OfferDTO> deleteOffer(@RequestHeader(name = "Authorization") String authorization, @RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok(adminOfferService.crudOperation(authorization, new OfferDTO(name, 0), DbbVerbs.DELETE));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}