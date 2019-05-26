package com.shorvat.recipe.controllers;

import com.shorvat.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {

 /*   private CategoryRepository categoryRepository;  // BootStrap - create repo to connect to DB, wire up  via Constructor
    private UnitOfMeasureRepository unitOfMeasureRepository;
    private RecipeRepository recipeRepository;

    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository,RecipeRepository recipeRepository ) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeRepository = recipeRepository;
    }*/

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/","index"})
    public String getIndexPage(Model model){
        log.debug("Getting Index page!");
        /*Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        //Optional<Recipe> recipeOptional = recipeRepository.fin*/



       /* // When the page is loaded from browser and mapping it will get repo object id and print it in console
        System.out.println("Cat Id is: " + categoryOptional.get().getId());
        System.out.println("Uom Id is: " + unitOfMeasureOptional.get().getId());*/

       model.addAttribute("recipes", recipeService.getRecipes());

        return "index";
    }
}
