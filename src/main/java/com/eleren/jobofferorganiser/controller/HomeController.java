package com.eleren.jobofferorganiser.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {

    @RequestMapping(path = "")
    public ResponseEntity<String> info(){
        return new ResponseEntity<>("Job offer organiser works", HttpStatus.OK);
    }
}
