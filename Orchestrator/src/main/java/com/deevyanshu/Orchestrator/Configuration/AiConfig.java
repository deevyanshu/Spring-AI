package com.deevyanshu.Orchestrator.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder)
    {
        return builder.build();
    }

    @Bean("Ai-executor")
    public Executor executor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(50);
        threadPoolTaskExecutor.setThreadNamePrefix("AI-worker-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
