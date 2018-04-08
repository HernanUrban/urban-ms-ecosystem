package com.urban.userservice.controller;

import com.urban.userservice.domain.User;
import com.urban.userservice.domain.UserType;
import com.urban.userservice.dto.NewUserRequest;
import com.urban.userservice.dto.UserResponse;
import com.urban.userservice.service.UserService;
import javax.security.auth.login.AccountException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity<?> create(@Valid @RequestBody NewUserRequest user) {
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

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("token") String token) {
        userService.verifyUserEmail(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
