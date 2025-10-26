package com.trivia.api.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.trivia.api.model.Questions;

@Repository
public interface IQuestionRepository extends MongoRepository<Questions, String>{
    
    @Aggregation(pipeline = {
        "{ $match: { grade: ?0 } }",
        "{ $sample: { size: 16 } }"
    })
    List<Questions> findRandom16ByGrade(String grade);

    boolean existsByQuestionText(String question);
}
