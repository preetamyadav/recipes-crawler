package com.crawler.crawler.model.mongo;

import com.crawler.crawler.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipeRepository extends MongoRepository<Recipe,String> {
    public Recipe findByName(String name);
}
