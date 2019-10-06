package com.shorvat.recipe.repositories;

import com.shorvat.recipe.domain.Ingredient;
import com.shorvat.recipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {


}
