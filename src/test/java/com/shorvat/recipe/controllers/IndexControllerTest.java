package com.shorvat.recipe.controllers;

import com.shorvat.recipe.domain.Recipe;
import com.shorvat.recipe.services.RecipeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
public class IndexControllerTest {

    // We need a mock service to access
    @Mock
    RecipeServiceImpl recipeService;

    // We need mock for  RecipeService repositoryModel
    @Mock
    Model model;

    IndexController indexController;

    @Before
    public void setUp() throws Exception {
        // setting mock recipe repository
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService); // creates instance of controller used for a test
    }

    @Test
    public void TestMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        // for get() import static method (alt+enter)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void getIndexPage() throws Exception {

        // given
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setDescription("First Recipe!");

        recipes.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipes);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        // when
        // check that getIndexPage() from indexController for model returns "index" model that is passed to TymeLeaf
        String viewName = indexController.getIndexPage(model);

        // then
        assertEquals("index",viewName);

        // check that getRecipes() on recipeService runs only 1 time
        verify(recipeService,times(1)).getRecipes();

        // check that addAttribure() method with String recipes and with anySet value
        verify(model,times(1)).addAttribute(eq("recipes"),anySet());
        // change anySet() with argumentCaptor
        verify(model,times(1)).addAttribute(eq("recipes"),argumentCaptor.capture());
        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2, setInController.size());

        log.debug("IndexControllerTest : getIndexPage() - OK!");

    }
}