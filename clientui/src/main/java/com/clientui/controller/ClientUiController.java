package com.clientui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientUiController {

    @GetMapping(value = "/")
    public String showHomeView(){
        return "home";
    }
}
