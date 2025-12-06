package com.tasktracker.model.service;

import com.tasktracker.model.dto.exception.AlreadyExistsException;
import com.tasktracker.model.dto.exception.InvalidCredentialsException;
import com.tasktracker.model.dto.exception.InvalidJwtException;
import com.tasktracker.model.dto.request.RefreshTokenRequest;
import com.tasktracker.model.dto.request.SignInRequest;
import com.tasktracker.model.dto.response.JwtAuthenticationResponse;
import com.tasktracker.model.dto.request.SignUpRequest;
import com.tasktracker.model.entity.RefreshToken;
import com.tasktracker.model.entity.Role;
import com.tasktracker.model.entity.User;
import com.tasktracker.model.repository.RefreshTokenRepository;
import com.tasktracker.model.repository.UserRepository;
import com.tasktracker.security.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) throws AlreadyExistsException {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new AlreadyExistsException("Пользователь с такой почтой уже зарегистрирован");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        refreshTokenRepository.save(new RefreshToken(refreshToken, user.getEmail()));

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    public JwtAuthenticationResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) throws InvalidJwtException {
        String token = refreshTokenRequest.token();
        String extractedUsername = jwtService.extractUsername(token);
        if (extractedUsername == null) {
            throw new InvalidJwtException();
        }

        UserDetails currentUserDetails = userDetailsService.loadUserByUsername(extractedUsername);
        RefreshToken refreshToken = refreshTokenRepository.findByValue(token);
        if (refreshToken == null || !jwtService.isTokenValid(token, currentUserDetails) ||
                !currentUserDetails.getUsername().equals(refreshToken.getOwnerUsername())) {
            throw new InvalidJwtException();
        }

        String newAccessToken = generateAccessToken(currentUserDetails);
        return new JwtAuthenticationResponse(newAccessToken, refreshToken.getValue());
    }

    private String generateAccessToken(UserDetails user) {
        return jwtService.generateToken(user, new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiration()));
    }

    private String generateRefreshToken(UserDetails user) {
        return jwtService.generateToken(user, new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration()));
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) throws InvalidJwtException, InvalidCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        RefreshToken oldToken = refreshTokenRepository.findByOwnerUsername(user.getUsername());
        if (oldToken == null) {
            throw new InvalidJwtException();
        }

        oldToken.setValue(refreshToken);
        refreshTokenRepository.save(oldToken);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }
}
