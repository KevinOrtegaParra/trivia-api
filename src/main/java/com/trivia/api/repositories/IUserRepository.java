package com.trivia.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.trivia.api.model.UserEntity;

@Repository
public interface IUserRepository extends MongoRepository<UserEntity, String>{
    
    Optional<UserEntity> findByName(String name);
    
    Optional<UserEntity> findByEmail(String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    List<UserEntity> findTop20ByOrderByPointsDesc();

    List<UserEntity> findAllByGradeOrderByPointsDesc(String grade);
}
