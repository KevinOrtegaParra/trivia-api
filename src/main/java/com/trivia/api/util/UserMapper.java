package com.trivia.api.util;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
//import org.springframework.stereotype.Component;

import com.trivia.api.dtos.user.UserRequestDTO;
import com.trivia.api.dtos.user.UserRespondeDTO;
import com.trivia.api.model.UserEntity;

//@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    
    UserEntity toUser(UserRequestDTO userRequestDTO);

    UserRespondeDTO toUserRespondeDTO(UserEntity user);

    List<UserRespondeDTO> toUserRespondeDTOList(List<UserEntity> userList);
}
