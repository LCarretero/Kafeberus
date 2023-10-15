package com.hiberus.controllers;

import com.hiberus.Exception.BadRequestException;
import com.hiberus.Exception.UnauthorizedException;
import com.hiberus.dto.OfferDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.services.AdminOfferService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offer")
@AllArgsConstructor

public class AdminOfferController {

    private AdminOfferService adminOfferService;

    @PostMapping("/create")
    public ResponseEntity<OfferDTO> createOffer(@RequestHeader(name = "Authorization") String Authorization, @RequestBody OfferDTO offer) {
        try {
            return ResponseEntity.ok(adminOfferService.crudOperation(Authorization, offer, DbbVerbs.POST));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<OfferDTO> deleteOffer(@RequestHeader(name = "Authorization") String Authorization, @RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok(adminOfferService.crudOperation(Authorization, new OfferDTO(name, 0), DbbVerbs.DELETE));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}