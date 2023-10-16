package com.hiberus.controllers;

import com.hiberus.exception.BadRequestException;
import com.hiberus.exception.UnauthorizedException;
import com.hiberus.dto.UserDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.services.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "API endpoints for user management")
public class AdminUserController {

    private AdminUserService adminUserService;

    @PostMapping("/create")
    @Operation(
            summary = "Create a user",
            description = "Create a new user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<UserDTO> createUser(@RequestHeader(name = "Authorization") String authorization, @RequestBody UserDTO user) {
        try {
            return ResponseEntity.ok(adminUserService.crudOperation(authorization, DbbVerbs.POST, user));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete a user",
            description = "Delete an existing user by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<UserDTO> deleteUser(@RequestHeader(name = "Authorization") String authorization, @RequestParam(name = "id") String id) {
        try {
            return ResponseEntity.ok(adminUserService.crudOperation(authorization, DbbVerbs.DELETE, new UserDTO(id, "", 0)));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    @Operation(
            summary = "Update a user",
            description = "Update an existing user by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<UserDTO> updateUser(@RequestHeader(name = "Authorization") String authorization, @RequestParam(name = "id") String id, @RequestBody UserDTO user) {
        try {
            return ResponseEntity.ok(adminUserService.crudOperation(authorization, DbbVerbs.PUT, new UserDTO(id, user.name(), user.points())));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
