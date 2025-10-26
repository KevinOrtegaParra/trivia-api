package com.trivia.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.trivia.api.model.Grade;

@Repository
public interface IGradeRepository extends MongoRepository<Grade, String>{
    boolean existsByName(String name);
}
