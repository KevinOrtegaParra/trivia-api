package com.trivia.api.services.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.trivia.api.dtos.user.UserRequestDTO;
import com.trivia.api.dtos.user.UserRequestUpdateDTO;
import com.trivia.api.dtos.user.UserRespondeDTO;
import com.trivia.api.exeption.ErrorDto;
import com.trivia.api.exeption.InternalServerErrorException;
import com.trivia.api.exeption.NotFoundException;
import com.trivia.api.exeption.RestException;
import com.trivia.api.model.AuthProvider;
import com.trivia.api.model.Role;
import com.trivia.api.model.UserEntity;
import com.trivia.api.repositories.IUserRepository;
import com.trivia.api.services.ifaces.IUserService;
import com.trivia.api.util.Messages;
import com.trivia.api.util.UserMapper;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UserRespondeDTO> getUsers() throws RestException {
        try {
            return userMapper.toUserRespondeDTOList(iUserRepository.findAll());
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
    public UserRespondeDTO update(UserRequestUpdateDTO userRequestUpdateDTO, Authentication authentication) {
        UserEntity user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));

        if (userRequestUpdateDTO.getName() != null)
            user.setName(userRequestUpdateDTO.getName());
        if (userRequestUpdateDTO.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userRequestUpdateDTO.getPassword()));
        if (userRequestUpdateDTO.getGrade() != null)
            user.setGrade(userRequestUpdateDTO.getGrade());
        user.setUpdateAt(LocalDateTime.now());

        try {
            return userMapper.toUserRespondeDTO(iUserRepository.save(user));
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
    public UserRespondeDTO getUser(String id) throws RestException {
        return userMapper.toUserRespondeDTO(iUserRepository.findById(id).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build())));
    }

    @Override
    public void delateId(String id) throws RestException {
        iUserRepository.deleteById(id);
    }

    @Override
    public UserRespondeDTO registerLocal(UserRequestDTO userRequestDTO) throws RestException {
        if (iUserRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("The email is already in use");
        }

        try {
            UserEntity user = userMapper.toUser(userRequestDTO);
            user.setRoles(Collections.singleton(Role.USER));// setRoles(Collections.singleton(new Role(2L)));
            String passwordEncoded = passwordEncoder.encode(user.getPassword());
            user.setPassword(passwordEncoded);
            user.setProvider(AuthProvider.LOCAL);
            user.setLives(3);
            user.setPoints(0);
            user.setCreateAt(LocalDateTime.now());

            return userMapper.toUserRespondeDTO(iUserRepository.save(user));

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
    public UserRespondeDTO loginWithGoogle(String Token) throws RestException {
        return null;
        /*
         * GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(token);
         * if (payload == null) {
         * throw new IllegalArgumentException("Token inválido");
         * }
         * 
         * String email = payload.getEmail();
         * String name = (String) payload.get("name");
         * 
         * return userRepository.findByEmail(email)
         * .orElseGet(() -> {
         * User newUser = new User();
         * newUser.setName(name);
         * newUser.setEmail(email);
         * newUser.setProvider(AuthProvider.GOOGLE);
         * newUser.setCreatedAt(LocalDateTime.now());
         * return userRepository.save(newUser);
         * });
         */
    }

    @Override
    public UserRespondeDTO livelife(Authentication authentication) throws RestException {
        UserEntity user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));

        if (user.getLives() < 3) {
            user.setLives(user.getLives() + 1);
        }
        ;
        user.setUpdateAt(LocalDateTime.now());

        try {
            return userMapper.toUserRespondeDTO(iUserRepository.save(user));
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

    private void recoverOneLife(UserEntity user) {
        if (user.getLives() < 3) {
            if (user.getLastLifeLostAt() == null ||
                    user.getLastLifeLostAt().isBefore(LocalDateTime.now().minusHours(5))) {

                user.setLives(user.getLives() + 1);
                user.setLastLifeLostAt(LocalDateTime.now());
                user.setUpdateAt(LocalDateTime.now());
                iUserRepository.save(user);
            }
        }
    }

    @Scheduled(fixedRate = 60000) // cada minuto
    public void recoverLivesForAllUsers() {
        List<UserEntity> users = iUserRepository.findAll();
        for (UserEntity user : users) {
            recoverOneLife(user);
        }
    }

    @Override
    public UserRespondeDTO loselife(Authentication authentication) throws RestException {
        UserEntity user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));

        if (user.getLives() > 0) {
            user.setLives(user.getLives() - 1);
            user.setLastLifeLostAt(LocalDateTime.now());
        }
        ;
        user.setUpdateAt(LocalDateTime.now());

        try {
            return userMapper.toUserRespondeDTO(iUserRepository.save(user));
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
    public UserRespondeDTO raisePoints(Integer puntos, Authentication authentication) throws RestException {
        UserEntity user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));

        user.setPoints(user.getPoints() + puntos);
        user.setUpdateAt(LocalDateTime.now());

        try {
            return userMapper.toUserRespondeDTO(iUserRepository.save(user));
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
    public List<UserRespondeDTO> ranking(Authentication authentication) throws RestException {
        UserEntity user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));

        try {
            return userMapper.toUserRespondeDTOList(iUserRepository.findAllByGradeOrderByPointsDesc(user.getGrade()));
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
    public UserRespondeDTO User(Authentication authentication) throws RestException {
         return userMapper.toUserRespondeDTO(iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build())));
    }

}
