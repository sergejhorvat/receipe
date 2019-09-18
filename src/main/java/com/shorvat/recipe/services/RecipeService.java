package com.shorvat.recipe.services;


import com.shorvat.recipe.commands.RecipeCommand;
import com.shorvat.recipe.domain.Recipe;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(Long l);

    RecipeCommand findCommandById(Long l);

    // @Transactional added when we have from RecipeServiceImpl added function definition.
    RecipeCommand saveRecipeCommand(RecipeCommand command);
}
