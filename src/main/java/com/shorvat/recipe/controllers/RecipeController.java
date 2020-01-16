package com.shorvat.recipe.controllers;

import com.shorvat.recipe.commands.RecipeCommand;
import com.shorvat.recipe.exceptions.NotFoundException;
import com.shorvat.recipe.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
public class RecipeController {

    private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
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
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command,
                               BindingResult bindingResult){  // result of the validation

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return RECIPE_RECIPEFORM_URL;
        }

        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

        // redirect to specific recipe url after save and persistance have been made
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{id}/delete")
    public String deleteById(@PathVariable String id){

        recipeService.deleteById(Long.valueOf(id));
        log.debug("Deleting id: " + id);
        return("redirect:/");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handlerNotFound(Exception exception){
        log.error("Handling 'not found' exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception); // To send exception to view
        return modelAndView;
    }


}
