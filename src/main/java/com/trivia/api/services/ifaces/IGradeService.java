package com.trivia.api.services.ifaces;

import java.util.List;

import com.trivia.api.dtos.grade.GradeRequestDTO;
import com.trivia.api.dtos.grade.GradeRespondeDTO;
import com.trivia.api.exeption.RestException;

public interface IGradeService {
    
    List<GradeRespondeDTO> getGrade() throws RestException;

    GradeRespondeDTO postGrade(GradeRequestDTO gradeRequestDTO) throws RestException;

    
}
