package com.shorvat.receipe.domain;

import javax.persistence.*;

@Entity
public class Notes {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne  // No cascade definition needed, it's defined in Recipe which is parent so when it's deleted the Note is also deleted.
    private Recipe receipe;

    @Lob // For large objects, like blob. JPA is goint to expect blob field in DB.
    private String recipeNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getReceipe() {
        return receipe;
    }

    public void setReceipe(Recipe receipe) {
        this.receipe = receipe;
    }

    public String getRecipeNote() {
        return recipeNote;
    }

    public void setRecipeNote(String recipeNote) {
        this.recipeNote = recipeNote;
    }
}
