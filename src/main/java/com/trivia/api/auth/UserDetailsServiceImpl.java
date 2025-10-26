package com.trivia.api.auth;

import java.time.LocalDateTime;
import java.util.Collection;
//import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trivia.api.exeption.ErrorDto;
import com.trivia.api.exeption.NotFoundException;
import com.trivia.api.model.UserEntity;
import com.trivia.api.repositories.IUserRepository;
import com.trivia.api.util.Messages;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));
        
        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.name())))
                .collect(Collectors.toList());
       
        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }

}