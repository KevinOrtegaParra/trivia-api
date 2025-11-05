package com.trivia.api.dtos.user;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserRequestDTO implements Serializable {

    static final long serialVersionUID = 1L;

    @NotBlank(message = "The name is mandatory")
    @Size(min = 3, max = 15, message = "The name must be at least 3 characters long and a maximum of 15 characters")
    String name;

    @NotBlank(message = "The email is mandatory")
    @Email(message = "The email must be valid")
    String email;

    @NotBlank(message = "The password is mandatory")
    @Size(min = 6, message = "The password must be at least 6 characters long")
    String password;

    @NotBlank(message = "the grade is mandatory")
    String grade;

}
