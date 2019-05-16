package com.shorvat.recipe.domain;

import javax.persistence.*;

@Entity
public class Notes {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne  // No cascade definition needed, it's defined in Recipe which is parent so when it's deleted the Note is also deleted.
    private Recipe recipe;

    @Lob // For large objects, like blob. JPA is going to expect blob field in DB.
    private String recipeNote;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getReceipe() {
        return recipe;
    }

    public void setReceipe(Recipe receipe) {
        this.recipe = receipe;
    }

    public String getRecipeNote() {
        return recipeNote;
    }

    public void setRecipeNote(String recipeNote) {
        this.recipeNote = recipeNote;
    }
}
