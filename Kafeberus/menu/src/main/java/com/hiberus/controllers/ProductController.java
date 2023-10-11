package com.hiberus.controllers;

import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class ProductController {


    private ProductService productService;

    @GetMapping("/getProduct")
    public ResponseEntity<ProductDTO> getProduct(@RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok(productService.getProduct(name));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
