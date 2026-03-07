package com.deevyanshu.Recipe.Service;

import com.deevyanshu.Recipe.Dto.Recipe;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private ChatClient chatClient;

    public RecipeService(ChatClient chatClient)
    {
        this.chatClient=chatClient;
    }

    public Recipe recipe(String query)
    {
        var systemplate= PromptTemplate.builder().template("You are a professional recipe generator and you have to generate the recipe")
                .build().createMessage().toString();

        return this.chatClient.prompt().system(systemplate).user(query).call().entity(Recipe.class);
    }
}
