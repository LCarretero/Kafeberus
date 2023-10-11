package com.hiberus.controllers;

import com.hiberus.Exception.BadRequestException;
import com.hiberus.Exception.UnauthorizedException;
import com.hiberus.dto.UpdateUser;
import com.hiberus.dto.UserDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.services.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestHeader(name = "Authorization") String Authorization, @RequestBody UserDTO user) {
        try {
            return ResponseEntity.ok(adminUserService.crudOperation(Authorization, DbbVerbs.POST, user));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserDTO> deleteUser(@RequestHeader(name = "Authorization") String Authorization, @RequestParam(name = "name") String name) {
        try {
            return ResponseEntity.ok(adminUserService.crudOperation(Authorization, DbbVerbs.DELETE, new UserDTO(null, name, 0)));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader(name = "Authorization") String Authorization, @RequestBody UserDTO user) {
        try {
            return ResponseEntity.ok(adminUserService.crudOperation(Authorization, DbbVerbs.PUT, user));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
