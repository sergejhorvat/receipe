package com.shorvat.recipe.services;

import com.shorvat.recipe.domain.Recipe;
import com.shorvat.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    public final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(Long recipeId, MultipartFile file) {

        try {
            // Get object from database because we will save existing recipe object
            Recipe recipe = recipeRepository.findById(recipeId).get();

            // Convert the multipart file to Byte object )java primitive to wrapper object)
            Byte[] byteObject = new Byte[file.getBytes().length];
            int i = 0;
            for (byte b : file.getBytes()) {
                byteObject[i++] = b;
            }

            recipe.setImage(byteObject);

            recipeRepository.save(recipe);
        }catch (IOException e) {
            // todo handle better
            log.error("Error saving image. ", e);
            e.printStackTrace();
        }
        log.debug("Received image file for recipe.");
    }
}
