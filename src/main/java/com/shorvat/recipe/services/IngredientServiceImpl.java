package com.shorvat.recipe.services;

import com.shorvat.recipe.commands.IngredientCommand;
import com.shorvat.recipe.converters.IngredientToIngredientCommand;
import com.shorvat.recipe.converters.IngredientCommandToIngredient;
import com.shorvat.recipe.domain.Ingredient;
import com.shorvat.recipe.domain.Recipe;
import com.shorvat.recipe.repositories.RecipeRepository;
import com.shorvat.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    public final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    public final RecipeRepository recipeRepository;
    public final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(!recipeOptional.isPresent()){
            // todo impement error handling
            log.error("recipe id not found: " + recipeId);
        }
        Recipe recipe = recipeOptional.get();

        // Utilize java steams to get ingredient and filter it based on ingredientId by id that was passed
        //   then map it over to ingredient command object it will return ingredientCommandOptional object
        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(!ingredientCommandOptional.isPresent()){
            // todo implement error handling
            log.error("Ingredient id not found: " + ingredientId);
        }
        return ingredientCommandOptional.get();
    }


    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(!recipeOptional.isPresent()){
            // todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            // If recipe ingredient does not exist create new one
            return new IngredientCommand();

        } else {
            // If recipe Optional ingredient exists get it
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals((command.getId())))
                    .findFirst();

            // If Ingredient is found
            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository
                    .findById(command.getUom().getId())
                    .orElseThrow(()-> new RuntimeException("UOM not found!"))); // todo address this
            } else {
                // Add new ingredient
                // make the relationship both ways
               Ingredient ingredient = ingredientCommandToIngredient.convert(command);
               ingredient.setRecipe(recipe);
               recipe.addIngredient(ingredient);
            }

            // Save receipe object with hibernate
            Recipe savedRecipe = recipeRepository.save(recipe);

            // Works if there is a persistent entity, but on brand new ingredient we do not have id value
            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            // check by description
            // we would test both in real world id and description
            if(!savedIngredientOptional.isPresent()){
                // not totaly safe but best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            // Return command object
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }
    }


    @Override
    @Transactional
    public void deleteIngredientById(Long recipeId, Long ingredientId){

        // Find the recipe that we want to delete ingredient
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if(ingredientOptional.isPresent()) {
                log.debug("Ingredient is found: " + ingredientId);
                Ingredient ingredientToDelete = ingredientOptional.get(); // get Ingredient property from recipe
                String deletedIngredientDesc =  ingredientToDelete.getDescription();
                ingredientToDelete.setRecipe(null); // break all bidirectional connections in persistance before delete
                recipe.getIngredients().remove(ingredientOptional.get()); // break all bidirectional connections in persistance before delete
                recipeRepository.save(recipe); // save new recipe state

                log.debug("DELETETED  Ingredient " + deletedIngredientDesc );
            }
        } else {
            log.debug("Recipe not found! id: " + recipeId);
        }



    }
}
