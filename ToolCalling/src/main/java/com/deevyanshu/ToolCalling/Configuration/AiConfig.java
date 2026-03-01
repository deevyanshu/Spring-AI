package com.deevyanshu.ToolCalling.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

        @Bean
    public ChatClient chatClient(ChatClient.Builder builder)
    {

        return builder.defaultOptions(GoogleGenAiChatOptions.builder()
                        .model("gemini-3-flash-preview").temperature(0.2)
                        .maxOutputTokens(1000).build()).build();
    }

}
