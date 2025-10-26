package com.trivia.api.util;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.trivia.api.dtos.grade.GradeRequestDTO;
import com.trivia.api.dtos.grade.GradeRespondeDTO;
import com.trivia.api.model.Grade;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GradeMapper {
    
    Grade toGrade(GradeRequestDTO gradeRequestDTO);

    GradeRespondeDTO toGradeRespondeDTO(Grade grade);

    List<GradeRespondeDTO> toGradeRespondeDTOList(List<Grade> gradeList);
}
