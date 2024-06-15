package com.banny.chaeggot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class MainController {

    @GetMapping
    public String getMainString() {
        return "Hello, Chaeggot!";
    }
}
