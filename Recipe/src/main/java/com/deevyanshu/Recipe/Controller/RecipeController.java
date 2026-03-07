package com.deevyanshu.Recipe.Controller;

import com.deevyanshu.Recipe.Dto.Recipe;
import com.deevyanshu.Recipe.Service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class RecipeController {

    private RecipeService service;

    public RecipeController(RecipeService recipeService)
    {
        this.service=recipeService;
    }

    @PostMapping("/recipe")
    public ResponseEntity<Recipe> generate(@RequestParam(value = "q",required = true) String query)
    {
        return ResponseEntity.ok(this.service.recipe(query));
    }

}
