package com.my.retail.catalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
public class HomeController {

    @RequestMapping("/")
    public String swaggerUI() {
        return "redirect:/swagger-ui.html";
    }
    
}
