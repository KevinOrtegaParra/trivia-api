package com.trivia.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Document(collection = "grade")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Grade {
    
    @Id
    @Setter(value = AccessLevel.NONE)
    String id;

    String name;

    String Description;

    @Field("create_at")
    LocalDateTime createAt;

    @Field("update_at")
    LocalDateTime updateAt;
}
