package com.mrm.meetingroommanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroqConfig {

    @Value("${GROQ_API_KEY}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
