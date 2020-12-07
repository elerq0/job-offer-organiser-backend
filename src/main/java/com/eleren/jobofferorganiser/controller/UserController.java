package com.eleren.jobofferorganiser.controller;

import com.eleren.jobofferorganiser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam String email) {
        try {
            userService.create(username, password, email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/login")
    public ResponseEntity<?> login(HttpServletResponse response,
                                        @RequestParam String username,
                                        @RequestParam String password) {
        try {
            response.setHeader("Authorization", userService.login(username, password));
            response.setHeader("Access-control-expose-headers", "Authorization");
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/current")
    public ResponseEntity<?> getUser() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return new ResponseEntity<>(userService.getByUsername(username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}