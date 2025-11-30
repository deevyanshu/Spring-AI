package com.deevyanshu.multimodal.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatModel(OpenAiChatModel openAiChatModel)
    {
        return ChatClient.builder(openAiChatModel).build();
    }

    @Bean(name = "geminiChatClient")
    public ChatClient genAiChatModel(GoogleGenAiChatModel genAiChatModel)
    {
        return ChatClient.builder(genAiChatModel).build();
    }
}
