package com.deevyanshu.websearch;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private ChatClient chatClient;

    public Controller(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "q",required = true) String q)
    {
        return this.chatClient.prompt(q).options(GoogleGenAiChatOptions.builder().googleSearchRetrieval(true).build()).call().content();
    }
}
