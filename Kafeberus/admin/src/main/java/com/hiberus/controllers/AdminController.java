package com.hiberus.controllers;

import com.hiberus.Exception.ProductBadRequestException;
import com.hiberus.Exception.UnauthorizedException;
import com.hiberus.dto.ProductDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    @PostMapping("/product/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestHeader(name = "Authorization") String Authorization, @RequestBody ProductCRUDValue product) {
        try {
            return ResponseEntity.ok(adminService.crudOperation(Authorization, product, DbbVerbs.POST));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (ProductBadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/product/delete")
    public ResponseEntity<ProductDTO> deleteProduct(@RequestHeader(name = "Authorization") String Authorization, @RequestBody String name) {
        try {
            return ResponseEntity.ok(adminService.crudOperation(Authorization, ProductCRUDValue.newBuilder().setName(name).build(), DbbVerbs.DELETE));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (ProductBadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/product/update")
    public ResponseEntity<ProductDTO> updateProduct(@RequestHeader(name = "Authorization") String Authorization, @RequestBody ProductCRUDValue product) {
        try {
            return ResponseEntity.ok(adminService.crudOperation(Authorization, product, DbbVerbs.PUT));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (ProductBadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
