package com.deevyanshu.parsingResponse.Service;

import com.deevyanshu.parsingResponse.Entity.Tut;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
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
        Prompt prompt1=new Prompt(prompt, GoogleGenAiChatOptions.builder().
                model("gemini-2.5-flash").temperature(0.2).maxOutputTokens(1000).build());

        List<Tut> tutorials=chatClient.prompt(prompt1).call().entity(new ParameterizedTypeReference<List<Tut>>() {
        });

        return tutorials;
    }
}
