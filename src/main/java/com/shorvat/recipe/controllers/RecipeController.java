package com.shorvat.recipe.controllers;

import com.shorvat.recipe.commands.RecipeCommand;
import com.shorvat.recipe.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    // Use {id} in @Pathvariable (Multiple variables could be used)
    public String showById(@PathVariable String id, Model model){

        // Adding model attribute called "recipe" and asking recipe Service to get that by id
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        return "recipe/show";
    }

    // Mapping for form view
    @GetMapping("recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe",recipeService.findCommandById(Long.valueOf(id)));
        return "recipe/recipeform";
    }

    // Mapping for saving post on form from Spring 4.3
    @PostMapping("recipe")
    // Other way to map post on "recipe" from Spring 4
    // @RequestMapping(value = "recipe", method = RequestMethod.POST)
    public String saveOrUpdate(@ModelAttribute RecipeCommand commands){
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(commands);

        // redirect to specific recipe url after save and persistance have been made
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{id}/delete")
    public String deleteById(@PathVariable String id){

        recipeService.deleteById(Long.valueOf(id));
        log.debug("Deleting id: " + id);
        return("redirect:/");
    }
}
