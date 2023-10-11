package com.hiberus.controllers;

import com.hiberus.Exception.BadRequestException;
import com.hiberus.Exception.UnauthorizedException;
import com.hiberus.dto.ProductDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.services.AdminProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class AdminProductController {

    private AdminProductService adminProductService;

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestHeader(name = "Authorization") String Authorization, @RequestBody ProductDTO product) {
        try {
            return ResponseEntity.ok(adminProductService.crudOperation(Authorization, product, DbbVerbs.POST));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ProductDTO> deleteProduct(@RequestHeader(name = "Authorization") String Authorization, @RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok(adminProductService.crudOperation(Authorization, new ProductDTO(name, 0F), DbbVerbs.DELETE));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDTO> updateProduct(@RequestHeader(name = "Authorization") String Authorization, @RequestBody ProductDTO product) {
        try {
            return ResponseEntity.ok(adminProductService.crudOperation(Authorization, product, DbbVerbs.PUT));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
