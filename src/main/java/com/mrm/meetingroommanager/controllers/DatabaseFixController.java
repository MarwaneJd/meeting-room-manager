package com.mrm.meetingroommanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/db-fix")
public class DatabaseFixController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseFixController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/reservation-sequence")
    public Map<String, Object> fixReservationSequence() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Read the SQL script from resources
            ClassPathResource resource = new ClassPathResource("db/fix-reservation-sequence.sql");
            Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            String sql = FileCopyUtils.copyToString(reader);

            // Split the SQL script into individual statements
            String[] statements = sql.split(";");

            // Execute each SQL statement
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    jdbcTemplate.execute(statement);
                }
            }

            // Verify the sequence exists
            String sequenceCheck = "SELECT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'reservation_seq')";
            boolean sequenceExists = jdbcTemplate.queryForObject(sequenceCheck, Boolean.class);

            response.put("success", true);
            response.put("message", "Reservation sequence fixed successfully");
            response.put("sequence_exists", sequenceExists);

            // Get current sequence value
            if (sequenceExists) {
                String currentValQuery = "SELECT last_value FROM reservation_seq";
                Long currentVal = jdbcTemplate.queryForObject(currentValQuery, Long.class);
                response.put("current_sequence_value", currentVal);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
