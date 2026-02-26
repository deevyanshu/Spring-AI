package com.deevyanshu.advisor.Config;

import com.deevyanshu.advisor.Advisos.TokenPrintAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    private Logger logger= LoggerFactory.getLogger(AiConfig.class);
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory)
    {
        this.logger.info("ChatMemoryImplementation class: "+chatMemory.getClass().getName());
        MessageChatMemoryAdvisor messageChatMemoryAdvisor=MessageChatMemoryAdvisor.builder(chatMemory).build();
        return builder.defaultOptions(GoogleGenAiChatOptions.builder()
                        .model("gemini-3-flash-preview").temperature(0.2)
                        .maxOutputTokens(1000)
                // SimpleLoggerAdvisor is prebuilt and tokenPrintadvisor is custom
                        .build()).defaultAdvisors(messageChatMemoryAdvisor, new TokenPrintAdvisor()/*,new SimpleLoggerAdvisor()*/)
                .build();
    }
}
