package org.binaracademy.challenge_7.controller;

import org.binaracademy.challenge_7.config.TokenProvider;
import org.binaracademy.challenge_7.entity.dto.UserDto;
import org.binaracademy.challenge_7.entity.request.LoginRequest;
import org.binaracademy.challenge_7.entity.response.Response;
import org.binaracademy.challenge_7.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    @Autowired
    public UserController(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService){
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @GetMapping(
            value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<Map<String, String>>> login(@RequestBody LoginRequest request){
        Response<Map<String, String>> response = new Response<>();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenProvider.generateToken(authentication);
        response.setError(false);
        response.setMessage("Login Berhasil");
        Map<String, String> dataToken = new HashMap<>();
        dataToken.put("token", token);
        response.setData(dataToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<UserDto>> register(@RequestBody UserDto newUser){
        return ResponseEntity.ok(userService.save(newUser));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping(
            value = "/update/user/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> updateUser(@PathVariable Long id,@RequestBody UserDto updateUser){
        return ResponseEntity.ok(userService.update(id, updateUser));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping(
            value = "/delete/user/{id}"
    )
    public ResponseEntity<Response<String>> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.delete(id));
    }
}
