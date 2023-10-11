package com.hiberus.controllers;

import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.services.ProductService;
import lombok.AllArgsConstructor;

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
