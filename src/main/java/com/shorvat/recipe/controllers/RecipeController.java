package com.shorvat.recipe.controllers;

import com.shorvat.recipe.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe/show/{id}")
    // Use {id} in @Pathvariable (Multiple variables could be used)
    public String showById(@PathVariable String id, Model model){

        // Adding model attribute called "recipe" and asking recipe Service to get that by id
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        return "recipe/show";
    }

   /* @RequestMapping("recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @PostMapping("recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand commands){
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(commands);

        return "redirect:/recipe/show/" + savedCommand.getId();
    }*/




}
