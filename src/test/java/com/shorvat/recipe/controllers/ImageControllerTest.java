package com.shorvat.recipe.controllers;


import com.shorvat.recipe.commands.RecipeCommand;
import com.shorvat.recipe.services.ImageService;
import com.shorvat.recipe.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;

    MockMvc mockMvc;

    @Before
    public void SetUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        // insert dependency manually
        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler()) // added not to be depenndant of Spring context, so UnitTest ccan be done in own
                .build();
    }

    @Test
    public void getImageForm() throws Exception {
        // given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        // When recipeService is asked to find any command return the local recipeCommand
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        // when - test MVC
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void handleImagePost() throws Exception{
        // Mock multi part file named imagefile
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile",
                        "Testing.txt",
                        "text/plain" ,
                        "Spring Framework Guru".getBytes());

        // Mock multipart option upload
        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location","/recipe/1/show"));

        //
        verify(imageService,times(1)).saveImageFile(anyLong(),any());
    }

    @Test
    public void renderImageFromDB() throws Exception{

        // given mock object
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        // Create byte array and conversion from  primitive to boxed version
        String s = "Fake image text";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];

        int i = 0;

        for (byte primByte : s.getBytes()){
            bytesBoxed[i++] = primByte;
        }

        recipeCommand.setImage(bytesBoxed);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseByte = response.getContentAsByteArray();

        assertEquals(s.getBytes().length, responseByte.length);
    }

    @Test
    public void testGetImageNumberFormatException() throws Exception{

        mockMvc.perform(get("/recipe/ads/recipeimage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}
