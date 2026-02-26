package com.deevyanshu.advisor.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {

        this.chatClient=chatClient;
    }

    public String chatTemplate(String q,String userId)
    {
        var systemPromptTemplate= SystemPromptTemplate.builder().template("You are a helpful coding assistant.  You are an expert in coding")
                .build();
        var systemMessage=systemPromptTemplate.createMessage();

        var userPromptTemplate= PromptTemplate.builder().template("{context}").build();
        var userMessage=userPromptTemplate.createMessage(Map.of(
                "context",q
        ));

        Prompt prompt=new Prompt(systemMessage,userMessage);
        var content=chatClient.prompt(prompt).advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,userId))
                .call().content();
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
}
