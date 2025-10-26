package com.trivia.api.dtos.question;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequestDTO implements Serializable{
    
    static final long serialVersionUID = 1L; 
    
    @NotBlank(message = "The question Text is mandatory")
    String questionText;

    @NotEmpty(message = "The options is mandatory")
    List<String> options;      

    @NotBlank(message = "The correct Answer is mandatory")
    String correctAnswer; 

    @NotBlank(message = "The grade is mandatory")
    String grade;
}
