package com.mrm.meetingroommanager.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void resetSequences() {
        try {
            // Reset the app_user_id_seq to a high value to avoid conflicts
            jdbcTemplate.execute("ALTER SEQUENCE IF EXISTS app_user_id_seq RESTART WITH 1000");
            jdbcTemplate.execute("ALTER SEQUENCE IF EXISTS role_id_seq RESTART WITH 1000");
            jdbcTemplate.execute("ALTER SEQUENCE IF EXISTS room_id_seq RESTART WITH 1000");
            jdbcTemplate.execute("ALTER SEQUENCE IF EXISTS reservation_id_seq RESTART WITH 1000");
        } catch (Exception e) {
            System.err.println("Error resetting sequences: " + e.getMessage());
        }
    }
}
