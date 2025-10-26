package com.trivia.api.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Document(collection = "user")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {

    @Id
    @Setter(AccessLevel.NONE)
    String id;

    String name;

    String email;      
    
    String password; 

    AuthProvider provider;

    Integer lives;

    @Field("last_life_lost_at")
    LocalDateTime lastLifeLostAt;

    Integer points;

    String grade;

    Set<Role> roles;

    @Field("create_at")
    LocalDateTime createAt;

    @Field("update_at")
    LocalDateTime updateAt;
}