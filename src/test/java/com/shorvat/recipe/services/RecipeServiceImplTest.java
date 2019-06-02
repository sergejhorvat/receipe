package com.shorvat.recipe.services;

import com.shorvat.recipe.domain.Recipe;
import com.shorvat.recipe.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    // We need a service to access
    RecipeServiceImpl recipeService;

    // set dependency to access repository and create mock repository
    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        // setting mock recipe repository
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    public void getRecipes() {

        // create recipe object and set to sent and retrive in test
        Recipe recipe = new Recipe();
        HashSet recipeData = new HashSet();
        recipeData.add(recipe);

        // Mockito - when recipeService getRecipes is called then return back recipeData,
        // so data is returned to test and we can do assert test
        when(recipeService.getRecipes()).thenReturn(recipeData);

        Set<Recipe> recipes = recipeService.getRecipes();

        assertEquals(recipes.size(),1);

        // Verify interaction that recipeRepository was called ONLY onde
        verify(recipeRepository,times(1)).findAll();
    }
}