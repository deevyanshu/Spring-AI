package com.fitness.aiservice.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder)
    {
        return builder.build();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClienctBuilder(){
        return WebClient.builder();
    }

    @Bean
    public WebClient activityServiceWebClient(WebClient.Builder builder)
    {
        return builder.baseUrl("http://ACTIVITYSERVICE").build();
    }
}
