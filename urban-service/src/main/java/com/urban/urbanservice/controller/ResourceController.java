package com.urban.urbanservice.controller;

import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

  @RequestMapping("/any")
  public ResponseEntity<?> home() {
    return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
  }

  @GetMapping("/me")
  public ResponseEntity<?> user(Principal user) {
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<String> adminResource(Principal user) {
    return new ResponseEntity<>("Hello " + user.getName(), HttpStatus.OK);
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<String> userResource(Principal user) {
    return new ResponseEntity<>("Hello " + user.getName(), HttpStatus.OK);
  }
}
