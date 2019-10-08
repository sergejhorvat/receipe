package com.shorvat.recipe.services;


import com.shorvat.recipe.domain.Recipe;
import com.shorvat.recipe.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    ImageService imageService;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    public void saveImageFile() throws Exception{
        // given
        Long id = 1L;
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile",
                        "Testing.txt",
                        "text/plain" ,
                        "Spring Framework Guru".getBytes());

        // Create recipe object and give Id
        Recipe recipe = new Recipe();
        recipe.setId(id);

        // Optional will be return bys the findById() method
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        // When
        imageService.saveImageFile(id, multipartFile);

        // Then
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        // Asset against saved recipe to check if the bytes are going to be the same
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }




}
