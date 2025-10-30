package com.trivia.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trivia.api.dtos.user.UserRequestDTO;
import com.trivia.api.dtos.user.UserRequestUpdateDTO;
import com.trivia.api.dtos.user.UserRespondeDTO;
import com.trivia.api.exeption.RestException;
import com.trivia.api.services.ifaces.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Users Controller", description = "Operations related to user management")
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    IUserService userService;

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Show all Users", description = "Endpoint to Show all users")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<UserRespondeDTO>> getUsers() throws RestException {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Show a user by their id", description = "Endpoint to Show a user by their id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<UserRespondeDTO> getUserById(@PathVariable String id) throws RestException {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Show a user ", description = "Endpoint to Show a user")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public ResponseEntity<UserRespondeDTO> getUser(Authentication authentication) throws RestException {
        return ResponseEntity.ok(userService.User(authentication));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Save a user", description = "Endpoint to save a user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<UserRespondeDTO> postUser(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerLocal(userRequestDTO)/* .register(userRequestDTO)*/);
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Update user", description = "End point to Update your user.")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping
    public ResponseEntity<UserRespondeDTO> putUser(@Valid @RequestBody UserRequestUpdateDTO requestUpdateDTO,
            Authentication authentication) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.update(requestUpdateDTO, authentication));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Delete a user", description = "Endpoint to delete a user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) throws RestException {
        userService.delateId(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Update live life user", description = "End point to Update your live life user.")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/liveLife")
    public ResponseEntity<UserRespondeDTO> putLiveLife(Authentication authentication) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.livelife(authentication));

    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Update lose live user", description = "End point to Update your lose live user.")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/loseLife")
    public ResponseEntity<UserRespondeDTO> putLoseLife(Authentication authentication) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.loselife(authentication));

    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Update raise Points", description = "End point to Update your raise Points.")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/raisePoints")
    public ResponseEntity<UserRespondeDTO> putRaisePoints(@RequestBody Integer point, Authentication authentication) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.raisePoints(point, authentication));

    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Show ranking Users", description = "Endpoint to Show ranking users")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ranking")
    public ResponseEntity<List<UserRespondeDTO>> getRanking(Authentication authentication) throws RestException {
        return ResponseEntity.ok(userService.ranking(authentication));
    }

}
