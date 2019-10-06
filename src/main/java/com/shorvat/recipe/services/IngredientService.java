package com.shorvat.recipe.services;


import com.shorvat.recipe.commands.IngredientCommand;
import com.shorvat.recipe.domain.Ingredient;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteIngredientById(Long recipeId, Long ingredientId);
}
