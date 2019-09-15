package com.shorvat.recipe.converters;


import com.shorvat.recipe.commands.IngredientCommand;
import com.shorvat.recipe.commands.NotesCommand;
import com.shorvat.recipe.domain.Ingredient;
import com.shorvat.recipe.domain.Notes;
import lombok.Synchronized;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Synchronized
    @Nullable
    @Override
    public Notes convert(NotesCommand source) {
        if(source == null) {
            return null;
        }

        final Notes notes = new Notes();
        notes.setId(source.getId());
        notes.setRecipeNote(source.getRecipeNotes());
        return notes;
    }
}
