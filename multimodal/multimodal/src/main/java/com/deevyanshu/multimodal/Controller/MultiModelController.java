package com.deevyanshu.multimodal.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultiModelController {

    private final ChatClient openAiChatClient;

    private final ChatClient geminiChatClient;

    //here we have created bean of ChatClient in configuration file
    public MultiModelController(@Qualifier("openAiChatClient") ChatClient openAiChatClient,@Qualifier("geminiChatClient") ChatClient geminiChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.geminiChatClient = geminiChatClient;
    }

    //    public MultiModelController(OpenAiChatModel openAiChatModel, GoogleGenAiChatModel genAiChatModel)
//    {
//        this.openAiChatClient=ChatClient.builder(openAiChatModel).build();
//        this.geminiChatClient=ChatClient.builder(genAiChatModel).build();
//    }

    @GetMapping("/chat")
    public ResponseEntity<String> test(@RequestParam(value = "prompt",required = true) String prompt)
    {
        String res=this.openAiChatClient.prompt(prompt).call().content();
        return ResponseEntity.ok(res);
    }
}
