package com.shorvat.recipe.services;

import com.shorvat.recipe.commands.RecipeCommand;
import com.shorvat.recipe.converters.RecipeCommandToRecipe;
import com.shorvat.recipe.converters.RecipeToRecipeCommand;
import com.shorvat.recipe.domain.Recipe;
import com.shorvat.recipe.exceptions.NotFoundException;
import com.shorvat.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.condition.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("I am the service!");  // From @Slf4j annotation from lombok

        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long l){

        Optional<Recipe> recipeOptional = recipeRepository.findById(l);

        if(!recipeOptional.isPresent()){
            //throw new RuntimeException("Recipe Not Found!");
            throw new NotFoundException("Recipe Not Found. For Id value: " + l.toString());
        }
        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long l){
        return recipeToRecipeCommand.convert(findById(l));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public void deleteById(Long idToDelete){
        // Calls Spring Data JPA repository to delete that from hibernate
        recipeRepository.deleteById(idToDelete);
    }

}
