package com.hiberus.controllers;

import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class ProductController {


    private ProductService productService;

    @GetMapping("/getProduct")
    public ResponseEntity<ProductDTO> getProduct(@RequestParam(name = "name" ) String name) {
        try {
            return ResponseEntity.ok(productService.getProduct(name));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
