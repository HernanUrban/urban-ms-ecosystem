package com.urban.userservice.controller;

import com.urban.userservice.domain.UserType;
import com.urban.userservice.dto.NewUserRequest;
import com.urban.userservice.dto.UserResponse;
import com.urban.userservice.error.UserNotFoundError;
import com.urban.userservice.error.UserServiceError;
import com.urban.userservice.error.UserValidationError;
import com.urban.userservice.service.UserService;
import io.swagger.annotations.*;
import jdk.nashorn.internal.runtime.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@Api(basePath = "/users", description = "User resources")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Get a User",
            notes = "Retrieves the User for a given account ID.\nAuthenticated user's role must be an Admin.",
            response = UserResponse.class,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> user(@PathVariable(name = "id", required = true) Long id) throws UserValidationError {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/me", produces = "application/json")
    @ApiOperation(value = "Get my User", notes = "Retrieves the User for the authenticated user.",
            response = UserResponse.class,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> me() throws UserValidationError {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    @ApiOperation(value = "Create a User", notes = "Creates a new user.\nNo authentication is required.",
            response = UserResponse.class,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> create(@Valid @RequestBody NewUserRequest user) throws UserServiceError {
        return new ResponseEntity<>(
                userService.create(user, UserType.USER), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "/delete", produces = "application/json")
    @ApiOperation(value = "Delete the User", notes = "Deletes the current user.",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser() throws UserNotFoundError {
        userService.deleteCurrentUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verify")
    @ApiOperation(value = "Activate a User",
            notes = "Activates the User.\nThis endpoint requires an activation code.\nNo authentication is required.",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verify(@RequestParam("code") String code) throws UserServiceError {
        userService.verifyUserEmail(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
