package com.emirsomuncu.HaveAnIdea.controllers;

import jdk.jfr.Category;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping("/")
    public String firstPage() {
        return "common_page";
    }


}
