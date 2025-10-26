package com.trivia.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.trivia.api.dtos.question.QuestionRequestDTO;
import com.trivia.api.dtos.question.QuestionUpdateDTO;
import com.trivia.api.dtos.question.QuiestionRespondeDTO;
import com.trivia.api.exeption.RestException;
import com.trivia.api.services.ifaces.IQuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Questions Controller", description = "Operations related to question management")
@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    IQuestionService questionService;

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Show all Questions", description = "Endpoint to Show all Questions")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<QuiestionRespondeDTO>> getQuestions(Authentication authentication) throws RestException {
        return ResponseEntity.ok(questionService.getQuestions(authentication));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Save a user", description = "Endpoint to save a user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<QuiestionRespondeDTO> postQuestion(@Valid @RequestBody QuestionRequestDTO questionRequestDTO)
            throws RestException {
        return ResponseEntity.ok(questionService.postQuestion(questionRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Update question", description = "End point to Update question.")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    public ResponseEntity<QuiestionRespondeDTO> putQuestion(@PathVariable String id,
            @Valid @RequestBody QuestionUpdateDTO requestUpdateDTO) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.update(id, requestUpdateDTO));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Delete a question", description = "Endpoint to delete a question")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String id) throws RestException {
        questionService.delateId(id);
        return ResponseEntity.noContent().build();
    }
}
