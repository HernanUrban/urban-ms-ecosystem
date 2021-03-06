package com.urban.authserver.controller;

import com.urban.authserver.domain.User;
import com.urban.authserver.domain.UserType;
import com.urban.authserver.dto.NewUserRequest;
import com.urban.authserver.dto.UserResponse;
import com.urban.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/me", produces = "application/json")
    public ResponseEntity<UserResponse> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody NewUserRequest user) {
        try {
            return new ResponseEntity<Object>(
                    userService.create(user, UserType.USER), HttpStatus.CREATED);
        } catch (AccountException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "/delete", produces = "application/json")
    public ResponseEntity<?> deleteUser() {
        try {
            userService.deleteCurrentUser();
            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
        }
    }
}
