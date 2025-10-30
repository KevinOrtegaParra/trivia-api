package com.trivia.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.trivia.api.dtos.grade.GradeRequestDTO;
import com.trivia.api.dtos.grade.GradeRespondeDTO;
import com.trivia.api.exeption.RestException;
import com.trivia.api.services.ifaces.IGradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Grades Controller", description = "Operations related to grade management")
@Controller
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    IGradeService gradeService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Show all grades", description = "Endpoint to Show all grades")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<GradeRespondeDTO>> getGrades() throws RestException {
        return ResponseEntity.ok(gradeService.getGrade());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Save a grade", description = "Endpoint to grade a user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<GradeRespondeDTO> postGrade(@Valid @RequestBody GradeRequestDTO gradeRequestDTO)
            throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.postGrade(gradeRequestDTO));
    }

}
