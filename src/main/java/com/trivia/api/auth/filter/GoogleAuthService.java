package com.trivia.api.auth.filter;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.trivia.api.auth.jwt.JwtUtils;
import com.trivia.api.model.AuthProvider;
import com.trivia.api.model.Role;
import com.trivia.api.model.UserEntity;
import com.trivia.api.repositories.IUserRepository;

@Service
public class GoogleAuthService {

    @Value("${google.client.id}")
    private String googleClientId;

    @Autowired
    private final IUserRepository userRepository;

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public GoogleAuthService(IUserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public String verifyAndGenerateToken(String idTokenString) throws Exception {
        var verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
               GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new RuntimeException("Invalid Google ID token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        // Buscar si el usuario ya existe
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        UserEntity user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // Crear usuario nuevo si no existe
            user = new UserEntity();
            user.setEmail(email);
            user.setName(name);
            String passwordEncoded = passwordEncoder.encode("");
            user.setPassword(passwordEncoded);
            user.setProvider(AuthProvider.GOOGLE);
            user.setLives(3);
            user.setPoints(0);
            user.setGrade(null);
            user.setRoles(Set.of(Role.USER));
            user.setCreateAt(java.time.LocalDateTime.now());
            userRepository.save(user);
        }

        // Generar JWT con tus utilidades
        return jwtUtils.generateAccesToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password("") // no importa, autenticación externa
                        .roles("USER")
                        .build()
        );
    }
}
