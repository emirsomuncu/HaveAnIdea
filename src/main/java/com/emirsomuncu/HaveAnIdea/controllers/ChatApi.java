package com.emirsomuncu.HaveAnIdea.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class ChatApi {

    private final ChatClient chatClient;

    @GetMapping(value = "/chat-with-stream-response", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithFlux(@RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content()
                .map(word -> word + " ");
    }
}
