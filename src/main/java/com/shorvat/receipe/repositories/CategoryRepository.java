package com.shorvat.receipe.repositories;

import com.shorvat.receipe.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category,Long> {
}
