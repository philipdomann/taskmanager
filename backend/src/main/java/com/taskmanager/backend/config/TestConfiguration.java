package com.taskmanager.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public DatabaseInitializer databaseInitializer() {
        return new DatabaseInitializer(null, null) {

            @Override
            public void run(String... args) {
                // Do nothing
            }
        };
    }

}
