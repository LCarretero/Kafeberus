package com.hiberus.controllers;

import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
@Tag(name = "Product Controller", description = "API endpoints for menu and product management")
public class ProductController {

    private ProductService productService;

    @GetMapping("/getProduct/{name}")
    @Operation(
            summary = "Get a product by name",
            description = "Get product details by its name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "name") String name) {
        try {
            return ResponseEntity.ok(productService.getProduct(name));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getProducts")
    @Operation(
            summary = "Get all products",
            description = "Get a list of all available products.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of products", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            }
    )
    public ResponseEntity<Iterable<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }
}
