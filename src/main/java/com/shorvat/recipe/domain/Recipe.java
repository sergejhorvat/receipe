package com.shorvat.recipe.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // support autogeration of sequences (most DB supports it)

    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;

    @Lob // needed more that 255 char so log
    private String directions;


    // OneToMany relationship from Recipe to Ingredient (One Recipe many Ingredients)
    // Receipe will be the owner of relationship, ingredient will not be able to navigate back to Recipe
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe") // Property on the child class(Ingredient) will be mapped by recipe field of child
    private Set<Ingredient> ingredients = new HashSet<>(); //initialize it not to have null pointer error

    @Lob // will be created as a blob in a DB.
    private Byte[] image;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToOne(cascade = CascadeType.ALL) // Recipe will be owner of this persistance, by deleting Recipe the Note will be deleted too.
    private Notes notes;

    @ManyToMany
    @JoinTable(name = "recipe_category",
        joinColumns = @JoinColumn(name = "recipe_id"), // this side
            inverseJoinColumns = @JoinColumn(name="category_id")) // other side
    private Set<Category> categories = new HashSet<>();  //initialize it not to have null pointer error


    public void setNotes(Notes notes) {
        // Insert null value checking if notes are not passed, no no null pointer error occurs.
        if(notes != null) {
            this.notes = notes;
            notes.setRecipe(this); // added to create bidirectional relationship between Notes nad Recipe while setting Note for recipe
        }
    }

    public Recipe addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
        ingredient.setRecipe(this);  // added to encapsulated logic in one place for bidirectional relationship
        return this;
    }
}
