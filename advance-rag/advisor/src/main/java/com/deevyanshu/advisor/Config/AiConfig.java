package com.deevyanshu.advisor.Config;

import com.deevyanshu.advisor.Advisos.TokenPrintAdvisor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
//import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiEmbeddingConnectionDetails;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingModel;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {



//    @Bean
//    public ChatClient chatClient(ChatClient.Builder builder)
//    {
//
//        return builder.defaultOptions(GoogleGenAiChatOptions.builder()
//                        .model("gemini-3-flash-preview").temperature(0.2)
//                        .maxOutputTokens(1000)
//                // SimpleLoggerAdvisor is prebuilt and tokenPrintadvisor is custom
//                        .build()).defaultAdvisors(/*new TokenPrintAdvisor()*//*,new SimpleLoggerAdvisor()*/)
//                .build();
//    }

    @Bean
    public GoogleGenAiTextEmbeddingModel embeddingModel(@Value("${spring.ai.google.genai.api-key}") String apiKey) {
        // Initialize the API client
        var api =  GoogleGenAiEmbeddingConnectionDetails.builder().apiKey(apiKey).build();
        // Configure options (model name, dimensions, etc.)
        var options = GoogleGenAiTextEmbeddingOptions.builder()
                .model("gemini-embedding-001")
                .dimensions(768)// Recommended for Gemini
                .build();

        return new GoogleGenAiTextEmbeddingModel(api, options);
    }
}
