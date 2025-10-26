package com.trivia.api.util;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.trivia.api.dtos.question.QuestionRequestDTO;
import com.trivia.api.dtos.question.QuiestionRespondeDTO;
import com.trivia.api.model.Questions;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {  
     
    Questions toQuestion(QuestionRequestDTO questionRequestDTO);

    QuiestionRespondeDTO toQuestionRespondeDTO(Questions questions);

    List<QuiestionRespondeDTO> toQuestionRespondeDTOList(List<Questions> questions);
}
