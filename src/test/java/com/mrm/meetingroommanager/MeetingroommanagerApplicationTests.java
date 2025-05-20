package com.mrm.meetingroommanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

@SpringBootTest
class MeetingroommanagerApplicationTests {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("ai.groq.api.key", () -> "test-api-key");
        registry.add("spring.datasource.url",
            () -> "jdbc:postgresql://localhost:5432/meeting_room_manager");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "Password@0@");
    }

    @Test
    void contextLoads() {
    }
}

