package com.deevyanshu.pdf_rag.Service;

import com.deevyanshu.pdf_rag.Advisor.ValidationAdvisor;
import io.pinecone.configs.PineconeConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.ai.vectorstore.pinecone.autoconfigure.PineconeVectorStoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PdfService {

    private ChatClient chatClient;
    private final ChatClient.Builder clientBuilder;

    @Autowired
    private PineconeVectorStore vectorStore;

    public PdfService(ChatClient.Builder clientBuilder) {
        this.clientBuilder=clientBuilder;
        this.chatClient=clientBuilder.build();
    }

    public String response(String q)
    {
        var advisor= QuestionAnswerAdvisor.builder(vectorStore).
                searchRequest(SearchRequest.builder().similarityThreshold(0.5d).
                        filterExpression("resume=='deevyanshu'").topK(3).build()).build();
        return this.chatClient.prompt(q).advisors(advisor,new ValidationAdvisor(clientBuilder)).call().content();
    }

}
