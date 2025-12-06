package com.tasktracker.model.dto.response;

import lombok.Getter;

@Getter
public class JwtAuthenticationResponse extends AbstractApiMessage {

    public JwtAuthenticationResponse(String accessToken, String refreshToken) {
        super("Аутентификация успешна.");
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    private final String accessToken;

    private final String refreshToken;
}
