package com.trivia.api.dtos.question;

import java.io.Serializable;
import java.util.List;

import com.google.auto.value.AutoValue.Builder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateDTO implements Serializable{
        
    static final long serialVersionUID = 1L; 

    String questionText;

    List<String> options;      

    String correctAnswer; 

    String grade;
}
