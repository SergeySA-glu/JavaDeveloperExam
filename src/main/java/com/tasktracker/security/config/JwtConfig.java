package com.tasktracker.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
@Getter
@AllArgsConstructor
public final class JwtConfig {
    private final String key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
}
