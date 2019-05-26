package com.shorvat.recipe.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Notes {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne  // No cascade definition needed, it's defined in Recipe which is parent so when it's deleted the Note is also deleted.
    private Recipe recipe;

    @Lob // For large objects, like blob. JPA is going to expect blob field in DB.
    private String recipeNote;


}
