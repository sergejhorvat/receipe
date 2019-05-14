package com.shorvat.receipe.controllers;

import com.shorvat.receipe.domain.Category;
import com.shorvat.receipe.domain.UnitOfMeasure;
import com.shorvat.receipe.repositories.CategoryRepository;
import com.shorvat.receipe.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {

    private CategoryRepository categoryRepository;  // create repo to connect to DB, wire up  via Constructor
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @RequestMapping({"","/","index"})
    public String getIndexPage(){

        Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");

        // When the page is loaded from browser and mapping it will get repo object id and print it in console
        System.out.println("Cat Id is: " + categoryOptional.get().getId());
        System.out.println("Uom Id is: " + unitOfMeasureOptional.get().getId());

        return "index";
    }
}
