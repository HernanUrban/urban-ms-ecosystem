package com.urban.authserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/check")
public class HealthCheckController {

    @RequestMapping("/secure")
    public String home() {
        return "Hello, this is a secure endpoint!";
    }


    @GetMapping("/open")
    @ResponseStatus(HttpStatus.OK)
    public String open() {
        return "Hi there! This is an open endpoint";
    }

}
