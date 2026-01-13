package com.deevyanshu.parsingResponse.Service;

import com.deevyanshu.parsingResponse.Entity.Tut;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {

//        this.chatClient=builder.build();

        //This is another way of adding prompt properties by default on all prompts
//        this.chatClient = builder.defaultOptions(GoogleGenAiChatOptions.builder()
//                        .model("gemini-2.5-flash").temperature(0.2)
//                        .maxOutputTokens(1000)
//                        .build())
//                .build();

        //another way is to create bean so that it can be used in all services
        this.chatClient=chatClient;
    }

    public Tut chat(String query){
        //here the user is the client sending the prompt and system is the llm model which behaves according to the text provided
//        String response=chatClient.prompt().user(query).system("you are an expert in cricket").call().content();
//        return response;

        Prompt prompt1=new Prompt(query);

//        var content=chatClient.prompt(prompt1)
//                .call()
//                .chatResponse()
//                .getResult()
//                .getOutput()
//                .getText();

        Tut content=chatClient.prompt(prompt1)
                .call()
                .entity(Tut.class);

        return content;
    }

    public List<Tut> chatList(String propt)
    {
        Prompt prompt1=new Prompt(propt);

        List<Tut> tutorials=chatClient.prompt(prompt1).call().entity(new ParameterizedTypeReference<List<Tut>>() {
        });

        return tutorials;
    }

    //this is for prompt properties
    public List<Tut> chatListPrompt(String prompt)
    {
        // this is one way
//        Prompt prompt1=new Prompt(prompt, GoogleGenAiChatOptions.builder().
//                model("gemini-2.5-flash").temperature(0.2).maxOutputTokens(1000).build());

        Prompt prompt1=new Prompt(prompt);
        List<Tut> tutorials=chatClient.prompt(prompt1).call().entity(new ParameterizedTypeReference<List<Tut>>() {
        });

        return tutorials;
    }

    //dynamic prompt templates
    public String chatTemplate()
    {
//        // first step:- template creation
//        PromptTemplate template=PromptTemplate.builder().template("What is {techname}? tell me an example of {examplename}").build();
//
//        //second step:- rendering
//        String renderMessage=template.render(Map.of("techname","spring","examplename","Spring boot"));
//
//        Prompt prompt=new Prompt(renderMessage);
//
//        var content=chatClient.prompt(prompt).call().content();
//        return content;

        var systemPromptTemplate=SystemPromptTemplate.builder().template("You are a helpful coding assistant.  You are an expert in coding")
                .build();
        var systemMessage=systemPromptTemplate.createMessage();

        var userPromptTemplate=PromptTemplate.builder().template("What is {techname}? tell me an example of {examplename}").build();
        var userMessage=userPromptTemplate.createMessage(Map.of(
                "techname","spring","examplename","Spring boot"
        ));

        Prompt prompt=new Prompt(systemMessage,userMessage);
        var content=chatClient.prompt(prompt).call().content();
        return content;
    }
}
