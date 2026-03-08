package com.deevyanshu.pdf_rag.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.google.genai.GoogleGenAiEmbeddingConnectionDetails;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingModel;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {



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
