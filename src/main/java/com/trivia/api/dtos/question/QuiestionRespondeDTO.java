package com.trivia.api.dtos.question;

import java.util.List;

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
public class QuiestionRespondeDTO {
     
    String id;
    
    String questionText;

    List<String> options;      
    
    String correctAnswer; 

    String grade;
}
