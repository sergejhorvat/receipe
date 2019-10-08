package com.shorvat.recipe.controllers;

import com.shorvat.recipe.services.ImageService;
import com.shorvat.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("recipe/{id}/image")
    public String ShowUploadForm(@PathVariable String id, Model model){
        log.debug("entered the ImageController GET for recipe/{id}/image");
        model.addAttribute("recipe",recipeService.findCommandById(Long.valueOf(id)));
        return("recipe/imageuploadform");
    }

    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile")MultipartFile file){
        imageService.saveImageFile(Long.valueOf(id), file);
        return("redirect:/recipe/" + id + "/show");
    }
}
