package com.shorvat.recipe.services;

import com.shorvat.recipe.commands.IngredientCommand;
import com.shorvat.recipe.converters.IngredientToIngredientCommand;
import com.shorvat.recipe.domain.Recipe;
import com.shorvat.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    public final IngredientToIngredientCommand ingredientToIngredientCommand;
    public final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(!recipeOptional.isPresent()){
            // todo impement error handling
            log.error("recipe id not found: " + recipeId);
        }
        Recipe recipe = recipeOptional.get();

        // Utilize java steams to get ingredient and filter it based on ingredientId by id that was passed
        //   then map it over to ingredient command object it will return ingredientCommandOptional object
        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(!ingredientCommandOptional.isPresent()){
            // todo implement error handling
            log.error("Ingredient id not found: " + ingredientId);
        }
        return ingredientCommandOptional.get();
    }
}
