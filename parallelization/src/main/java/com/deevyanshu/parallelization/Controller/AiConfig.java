package com.deevyanshu.parallelization.Controller;

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

    @Bean("ai-executor")
    public Executor executor()
    {
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("AI-worker-");
        executor.initialize();
        return executor;
    }
}
