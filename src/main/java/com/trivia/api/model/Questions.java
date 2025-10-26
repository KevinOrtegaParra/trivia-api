package com.trivia.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Document(collection = "question")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Questions implements Serializable{
    
    static final long serialVersionUID = 1L;

    @Id
    @Setter(AccessLevel.NONE)
    String id;
    
    @Field("question_text")
    String questionText;

    List<String> options;      
    
    @Field("correct_answer")
    String correctAnswer; 

    String grade;

    @Field("create_at")
    LocalDateTime createAt;

    @Field("update_at")
    LocalDateTime updateAt;
}
