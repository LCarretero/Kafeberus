package com.hiberus.controllers;

import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.ProductBadRequest;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.models.Product;
import com.hiberus.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/menu")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.createProduct(product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ProductBadRequest e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id") String id) {
        try {
            return ResponseEntity.ok(productService.getProduct(UUID.fromString(id)));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
