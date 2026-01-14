package com.deevyanshu.advisor.Config;

import com.deevyanshu.advisor.Advisos.TokenPrintAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder)
    {
        return builder.defaultOptions(GoogleGenAiChatOptions.builder()
                        .model("gemini-2.5-flash").temperature(0.2)
                        .maxOutputTokens(1000)
                // SimpleLoggerAdvisor is prebuilt and tokenPrintadvisor is custom
                        .build()).defaultAdvisors(new TokenPrintAdvisor()/*,new SimpleLoggerAdvisor()*/)
                .build();
    }
}
