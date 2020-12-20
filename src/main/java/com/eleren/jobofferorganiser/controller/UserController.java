package com.eleren.jobofferorganiser.controller;

import com.eleren.jobofferorganiser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> user) {
        try {
            userService.create(user.get("username"),
                    user.get("password"),
                    user.get("email"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(path = "/login")
    public ResponseEntity<?> login(HttpServletResponse response, @RequestBody Map<String, String> user) {
        try {
            response.setHeader("Authorization",
                    userService.login(user.get("username"), user.get("password")));
            response.setHeader("Access-control-expose-headers",
                    "Authorization");
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