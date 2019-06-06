package com.shorvat.recipe.repositories;

import com.shorvat.recipe.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest // that will bring up embedded database and configure Spring data JPA
public class UnitOfMeasureRepositoryIT {

    @Autowired  // Spring will do dependency injection on our integration test.
            // Spring context will start up and we will get instance of unitOfMeasureRepository injected
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() throws Exception {
    }

     @Test
     public void findByDescription() throws Exception{
         Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
         // well ask repo to look up by teaspoon
         assertEquals("Teaspoon",uomOptional.get().getDescription());

     }
}