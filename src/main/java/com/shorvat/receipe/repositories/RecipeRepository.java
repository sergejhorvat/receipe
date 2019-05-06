package com.shorvat.receipe.repositories;

import com.shorvat.receipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
