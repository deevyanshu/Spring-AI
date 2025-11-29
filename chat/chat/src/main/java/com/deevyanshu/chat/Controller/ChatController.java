package com.deevyanshu.chat.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private  final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatBuilder)
    {
        this.chatClient=chatBuilder.build();
    }

    @GetMapping("/chat")
    public ResponseEntity<String> generateChat(@RequestParam(value = "prompt", required = true) String prompt)
    {
        String response=chatClient.prompt(prompt).call().content();
        return ResponseEntity.ok(response);
    }
}
