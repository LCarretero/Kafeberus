package com.hiberus.controllers;

import com.hiberus.exception.BadRequestException;
import com.hiberus.exception.UnauthorizedException;
import com.hiberus.dto.ProductDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.services.AdminProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
@Tag(name = "Product Controller", description = "API endpoints for product management")
public class AdminProductController {

    private AdminProductService adminProductService;

    @PostMapping("/create")
    @Operation(
            summary = "Create a product",
            description = "Create a new product.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<ProductDTO> createProduct(@RequestHeader(name = "Authorization") String authorization, @RequestBody ProductDTO product) {
        try {
            return ResponseEntity.ok(adminProductService.crudOperation(authorization, product, DbbVerbs.POST));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete a product",
            description = "Delete an existing product by name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<ProductDTO> deleteProduct(@RequestHeader(name = "Authorization") String authorization, @RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok(adminProductService.crudOperation(authorization, new ProductDTO(name, 0F), DbbVerbs.DELETE));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    @Operation(
            summary = "Update a product",
            description = "Update an existing product.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated", content = @Content(mediaType = "application.json", schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<ProductDTO> updateProduct(@RequestHeader(name = "Authorization") String authorization, @RequestBody ProductDTO product) {
        try {
            return ResponseEntity.ok(adminProductService.crudOperation(authorization, product, DbbVerbs.PUT));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
