package com.shorvat.recipe.controllers;


import com.shorvat.recipe.commands.IngredientCommand;
import com.shorvat.recipe.commands.RecipeCommand;
import com.shorvat.recipe.commands.UnitOfMeasureCommand;
import com.shorvat.recipe.domain.Ingredient;
import com.shorvat.recipe.services.IngredientService;
import com.shorvat.recipe.services.RecipeService;

import com.shorvat.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService,
                                IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService){
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list from recipe id: "  + recipeId);

        // Use command object to avoid lazy load error in Thymeleaf

        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return("recipe/ingredient/list");
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String id,
                                       Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        return("recipe/ingredient/show");
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/new")
    public String newRecipe(@PathVariable String recipeId, Model model){

        // make shure toi have good id value
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

        // need to return back parent id for hidden form property
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        model.addAttribute("ingredient", ingredientCommand);

        // init uom
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        // return view
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id,
                                         Model model){
        model.addAttribute("ingredient",
                ingredientService.findByRecipeIdAndIngredientId(
                        Long.valueOf(recipeId),
                        Long.valueOf(id))
        );

        model.addAttribute("uomList",
                unitOfMeasureService.listAllUoms());
        return("recipe/ingredient/ingredientform");
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved recipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id: " + savedCommand.getId());

        return("redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show");
    }


    // homework delete ingredient
    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id){

       // Call service to delete ingredient by values of is
        ingredientService.deleteIngredientById(Long.valueOf(recipeId),
                                                Long.valueOf(id));


        // Return list of ingredients for recipe
        return("redirect:/recipe/" + recipeId + "/ingredients/");

    }

}
