package com.fitness.aiservice.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    private final ChatClient chatClient;

    public GeminiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String response(String details)
    {
        return this.chatClient.prompt(details).call().content();
    }
}
