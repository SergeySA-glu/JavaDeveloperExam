package com.tasktracker.security.config;

import com.tasktracker.model.entity.Role;
import com.tasktracker.model.entity.User;
import com.tasktracker.model.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializerConfig {

    @Bean
    public CommandLineRunner initializeData(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Cоздаем админа, если его нет
            if (userRepository.findByEmail("admin").isEmpty()) {
                User admin = User.builder()
                        .email("admin")
                        .passwordHash(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
