package com.tasktracker.controller;

import com.tasktracker.model.dto.exception.AlreadyExistsException;
import com.tasktracker.model.dto.exception.InvalidCredentialsException;
import com.tasktracker.model.dto.exception.InvalidJwtException;
import com.tasktracker.model.dto.request.RefreshTokenRequest;
import com.tasktracker.model.dto.request.SignInRequest;
import com.tasktracker.model.dto.request.SignUpRequest;
import com.tasktracker.model.dto.response.JwtAuthenticationResponse;
import com.tasktracker.model.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Регистрация и вход для пользователя")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest signUpRequest) throws AlreadyExistsException {
        return authenticationService.signUp(signUpRequest);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest signInRequest) throws InvalidJwtException, InvalidCredentialsException {
        return authenticationService.signIn(signInRequest);
    }

    @PostMapping("/refresh")
    public JwtAuthenticationResponse refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws InvalidJwtException {
        return authenticationService.refreshAccessToken(refreshTokenRequest);
    }
}
