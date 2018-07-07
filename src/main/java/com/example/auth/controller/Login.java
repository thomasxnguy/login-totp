package com.example.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login {
    // Login form
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    // Access page
    @RequestMapping("/helloworld")
    public String helloworld() {
        return "helloworld.html";
    }

}
