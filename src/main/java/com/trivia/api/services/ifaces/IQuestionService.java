package com.trivia.api.services.ifaces;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.trivia.api.dtos.question.QuestionRequestDTO;
import com.trivia.api.dtos.question.QuestionUpdateDTO;
import com.trivia.api.dtos.question.QuiestionRespondeDTO;
import com.trivia.api.exeption.RestException;

public interface IQuestionService {
    
    List<QuiestionRespondeDTO> getQuestions(Authentication authentication) throws RestException;

    QuiestionRespondeDTO postQuestion(QuestionRequestDTO questionRequestDTO) throws RestException;

    QuiestionRespondeDTO update(String id, QuestionUpdateDTO questionUpdateDTO)throws RestException;

    void delateId(String id) throws RestException;

}
