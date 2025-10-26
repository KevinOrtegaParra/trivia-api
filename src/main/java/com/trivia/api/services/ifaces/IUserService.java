package com.trivia.api.services.ifaces;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.trivia.api.dtos.user.UserRequestDTO;
import com.trivia.api.dtos.user.UserRequestUpdateDTO;
import com.trivia.api.dtos.user.UserRespondeDTO;
import com.trivia.api.exeption.RestException;

public interface IUserService {
    
    List<UserRespondeDTO> getUsers() throws RestException;

    UserRespondeDTO registerLocal(UserRequestDTO userRequestDTO) throws RestException;

    UserRespondeDTO loginWithGoogle(String Token) throws RestException;

    UserRespondeDTO update(UserRequestUpdateDTO userRequestUpdateDTO, Authentication authentication)throws RestException;;

    UserRespondeDTO getUser(String id) throws RestException;

    void delateId(String id) throws RestException;

    UserRespondeDTO livelife(Authentication authentication)throws RestException;

    UserRespondeDTO loselife(Authentication authentication)throws RestException;

    UserRespondeDTO raisePoints(Integer puntos, Authentication authentication)throws RestException;

    List<UserRespondeDTO> ranking(Authentication authentication)throws RestException;
}
