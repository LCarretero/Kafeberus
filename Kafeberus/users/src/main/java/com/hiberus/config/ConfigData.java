package com.hiberus.config;

import com.hiberus.models.User;
import com.hiberus.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ConfigData {
    @Bean("ConfigData")
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            User god = User.builder()
                    .name("Lisardo")
                    .id(UUID.fromString("9417eab7-abc0-4895-82fe-aa6e40c7f683"))
                    .fidelity(0)
                    .build();

            userRepository.save(god);
        };
    }
}
