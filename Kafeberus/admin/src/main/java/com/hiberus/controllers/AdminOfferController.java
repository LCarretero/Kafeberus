package com.hiberus.controllers;

import com.hiberus.Exception.BadRequestException;
import com.hiberus.Exception.UnauthorizedException;
import com.hiberus.dto.OfferDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.services.AdminOfferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offer")
@Api(value = "Admin Offer Controller", tags = {"Admin Offer"})
public class AdminOfferController {
    @Autowired
    private AdminOfferService adminOfferService;

    @PostMapping("/create")
    @ApiOperation(value = "Create an offer", notes = "Creates a new offer in the system")
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
    @ApiOperation(value = "Delete an offer", notes = "Deletes an offer from the system")
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