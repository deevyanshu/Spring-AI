package com.audio.audiotranscribe.Controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audio.audiotranscribe.Service.ChatService;
import com.audio.audiotranscribe.Service.ImageService;
import com.audio.audiotranscribe.Service.RecipeService;


@RestController
@RequestMapping("api/gen")
public class TranscriptionController {

    @Autowired
    private ChatService service;

    @Autowired
    private ImageService imageService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/ask-ai")
    public String getResponse(@RequestParam String prompt)
    {
        return service.getResponse(prompt);

    }

    @GetMapping("/generate-image")
    public List<String> generateImages(@RequestParam String prompt)
    {
        ImageResponse response=imageService.imageResponse(prompt);
        List<String> imageUrls=response.getResults().stream().map(result-> result.getOutput().getUrl()).collect(Collectors.toList());

        return imageUrls;
    }

    @GetMapping("/recipe")
    public String generateRecipe(@RequestParam String ingredients, @RequestParam(defaultValue = "any") String cuisine, @RequestParam(defaultValue = "") String dieteryRestrictions)
    {
        return recipeService.createReccipe(ingredients, cuisine, dieteryRestrictions);
    }




}
