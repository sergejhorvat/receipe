package com.shorvat.recipe.converters;

import com.shorvat.recipe.commands.UnitOfMeasureCommand;
import com.shorvat.recipe.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure>{

    // Spring does not guarantee thread safety, so using project Loombok Synchronized
    // method that is thread safe in multi thread environment

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureCommand source) {
        if (source == null) {
            // Able to return null
            return null;
        }

        // Declaring variables final so they are immutable, giving more code security
        final UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(source.getId());
        uom.setDescription(source.getDescription());
        // Returns instance of converted type
        return uom;
    }
}
