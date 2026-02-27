package com.deevyanshu.advisor.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatClient chatClient;

    private VectorStore vectorStore;

    public ChatService(ChatClient chatClient, VectorStore vectorStore) {

        this.chatClient=chatClient;
        this.vectorStore=vectorStore;
    }

    public String chatTemplate(String q,String userId)
    {
        //load data from vector database
        SearchRequest searchRequest= SearchRequest.builder()
                .topK(5)
                .similarityThreshold(0.6)
                .query(q)
                .build();

        List<Document> documents=this.vectorStore.similaritySearch(searchRequest);
        List<String> documentList=documents.stream().map(item->item.getText()).toList();
        String context=String.join(",",documentList);
        //similar result user query

        var systemPromptTemplate= SystemPromptTemplate.builder().
                template("You are a coding assistant. Explain concepts clearly with examples. Answer only from DOCUMENTS section. If something is not in DOCUMENTS, reply with I don't know. DOCUMENTS:{documents}")
                .build();
        var systemMessage=systemPromptTemplate.createMessage(Map.of(
                "documents",context
        ));

        var userPromptTemplate= PromptTemplate.builder().template("{context}").build();
        var userMessage=userPromptTemplate.createMessage(Map.of(
                "context",q
        ));

        Prompt prompt=new Prompt(systemMessage,userMessage);
        var content=chatClient.prompt(prompt).call().content();
        return content;
    }

    public Flux<String> streamChat() {
        var systemPromptTemplate= SystemPromptTemplate.builder().template("You are a helpful coding assistant.  You are an expert in coding")
                .build();
        var systemMessage=systemPromptTemplate.createMessage();

        var userPromptTemplate= PromptTemplate.builder().template("What is {techname}? tell me an example of {examplename}").build();
        var userMessage=userPromptTemplate.createMessage(Map.of(
                "techname","spring","examplename","Spring boot"
        ));

        Prompt prompt=new Prompt(systemMessage,userMessage);
        return chatClient.prompt(prompt).stream().content();

    }

    //for dumping data to vecctor database
    public void saveData(List<String> list)
    {
        List<Document> documentList=list.stream().map(item->new Document(item)).collect(Collectors.toList());
        this.vectorStore.add(documentList);
    }
}
