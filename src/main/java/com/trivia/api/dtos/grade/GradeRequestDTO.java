package com.trivia.api.dtos.grade;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
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
public class GradeRequestDTO implements Serializable{
    
    static final long serialVersionUID = 1L; 

    @NotBlank(message = "The name is mandatory")
    String name;

    String Description;
}
