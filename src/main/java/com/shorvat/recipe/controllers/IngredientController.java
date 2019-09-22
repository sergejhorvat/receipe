package com.shorvat.recipe.controllers;


import com.shorvat.recipe.services.RecipeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;

    public IngredientController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list from recipe id: "  + recipeId);

        // Use command object to avoid lazy load error in Thymeleaf

        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return("recipe/ingredient/list");

    }
}
