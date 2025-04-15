package com.emirsomuncu.HaveAnIdea.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chat-stream")
    public String chatStreamPage() {
        return "/user/chat-stream";
    }
}
