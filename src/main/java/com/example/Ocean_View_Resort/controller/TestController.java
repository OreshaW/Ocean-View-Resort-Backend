package com.example.Ocean_View_Resort.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Ocean View Resort Backend Running Successfully!";
    }

    @GetMapping("/test")
    public String test() {
        return "Test API Working!";
    }
}