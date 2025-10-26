package com.trivia.api.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.trivia.api.dtos.grade.GradeRequestDTO;
import com.trivia.api.dtos.grade.GradeRespondeDTO;
import com.trivia.api.exeption.ErrorDto;
import com.trivia.api.exeption.InternalServerErrorException;
import com.trivia.api.exeption.RestException;
import com.trivia.api.model.Grade;
import com.trivia.api.repositories.IGradeRepository;
import com.trivia.api.services.ifaces.IGradeService;
import com.trivia.api.util.GradeMapper;
import com.trivia.api.util.Messages;

@Service
public class GradeServiceImpl implements IGradeService{
    
    @Autowired
    IGradeRepository gradeRepository;

    @Autowired
    GradeMapper gradeMapper;

    @Override
    public List<GradeRespondeDTO> getGrade() throws RestException {
         try {
            return gradeMapper.toGradeRespondeDTOList(gradeRepository.findAll());
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
    public GradeRespondeDTO postGrade(GradeRequestDTO gradeRequestDTO) throws RestException {
        if (gradeRepository.existsByName(gradeRequestDTO.getName())) {
            throw new IllegalArgumentException("The name is already in use");
        }

        try {
            Grade grade = gradeMapper.toGrade(gradeRequestDTO);
            grade.setCreateAt(LocalDateTime.now());

            return gradeMapper.toGradeRespondeDTO(gradeRepository.save(grade));

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



}
