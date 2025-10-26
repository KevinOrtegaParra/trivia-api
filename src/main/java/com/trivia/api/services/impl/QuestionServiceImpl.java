package com.trivia.api.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.trivia.api.dtos.question.QuestionRequestDTO;
import com.trivia.api.dtos.question.QuestionUpdateDTO;
import com.trivia.api.dtos.question.QuiestionRespondeDTO;
import com.trivia.api.exeption.ErrorDto;
import com.trivia.api.exeption.InternalServerErrorException;
import com.trivia.api.exeption.NotFoundException;
import com.trivia.api.exeption.RestException;
import com.trivia.api.model.Questions;
import com.trivia.api.model.UserEntity;
import com.trivia.api.repositories.IQuestionRepository;
import com.trivia.api.repositories.IUserRepository;
import com.trivia.api.services.ifaces.IQuestionService;
import com.trivia.api.util.Messages;
import com.trivia.api.util.QuestionMapper;

@Service
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    private IQuestionRepository iQuestionRepository;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<QuiestionRespondeDTO> getQuestions(Authentication authentication) throws RestException {

        UserEntity user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));
        try {
            if (user.getLives() > 0)
                return questionMapper
                        .toQuestionRespondeDTOList(iQuestionRepository.findRandom16ByGrade(user.getGrade()));
            return null;
        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
    }

    @Override
    public QuiestionRespondeDTO postQuestion(QuestionRequestDTO questionRequestDTO) throws RestException {
        if (iQuestionRepository.existsByQuestionText(questionRequestDTO.getQuestionText())) {
            throw new IllegalArgumentException("The question exists");
        }

        try {
            Questions questions = questionMapper.toQuestion(questionRequestDTO);
            questions.setCreateAt(LocalDateTime.now());

            return questionMapper.toQuestionRespondeDTO(iQuestionRepository.save(questions));

        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
    }

    @Override
    public QuiestionRespondeDTO update(String id, QuestionUpdateDTO questionUpdateDTO) throws RestException {

        Questions questions = iQuestionRepository.findById(id).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.QUESTION_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));

        try {

            if (questionUpdateDTO.getQuestionText() != null)
                questions.setQuestionText(questionUpdateDTO.getQuestionText());
            if (questionUpdateDTO.getOptions() != null)
                questions.setOptions(questionUpdateDTO.getOptions());
            if (questionUpdateDTO.getCorrectAnswer() != null)
                questions.setCorrectAnswer(questionUpdateDTO.getCorrectAnswer());
            if (questionUpdateDTO.getGrade() != null)
                questions.setGrade(questionUpdateDTO.getGrade());
            questions.setCreateAt(LocalDateTime.now());

            return questionMapper.toQuestionRespondeDTO(iQuestionRepository.save(questions));

        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
    }

    @Override
    public void delateId(String id) throws RestException {
        iQuestionRepository.deleteById(id);
    }

}
