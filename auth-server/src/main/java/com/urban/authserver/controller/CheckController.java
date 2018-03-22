package com.urban.authserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/check")
public class CheckController {

    @RequestMapping("/secure")
    public String home() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return String.format("Hello %s, this is a secure endpoint!", username);
    }


    @GetMapping("/open")
    @ResponseStatus(HttpStatus.OK)
    public String open() {
        return "Hi there! This is an open endpoint";
    }

}
