package com.tasktracker.security.config;

import com.tasktracker.security.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class JwtSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final AuthenticationFilter filter;

    public JwtSecurityConfig(UserDetailsService userDetailsService, AuthenticationFilter filter) {
        this.userDetailsService = userDetailsService;
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider provider) throws Exception {
        return http.csrf(
                AbstractHttpConfigurer::disable
        ).cors(
                (httpSecurityCorsConfigurer) -> httpSecurityCorsConfigurer.configurationSource(
                        request -> {
                            var corsConfig = new CorsConfiguration();
                            corsConfig.setAllowedMethods(
                                    List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
                            );
                            corsConfig.setAllowedOriginPatterns(
                                    List.of("*")
                            );
                            corsConfig.setAllowedHeaders(
                                    List.of("*")
                            );
                            corsConfig.setAllowCredentials(true);
                            return corsConfig;
                        }
                )
        ).authorizeHttpRequests(
                (authorizationManagerRequestMatcherRegistry) ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/tasks/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/tasks/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/tasks/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAnyRole("USER", "ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/task-groups/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/task-groups/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/task-groups/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/task-groups/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers(
                                        "/actuator",
                                        "/actuator/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-resources/*",
                                        "/error",
                                        "/api-docs/**",
                                        "/api-docs",
                                        "/swagger-ui/**",
                                        "/v3/api-docs*/**"
                                ).permitAll()
                                .anyRequest().authenticated()
        ).sessionManagement(
                (httpSecuritySessionManagementConfigurer) ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ).authenticationProvider(
                provider
        ).addFilterBefore(
                filter, UsernamePasswordAuthenticationFilter.class
        ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder encoder) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
